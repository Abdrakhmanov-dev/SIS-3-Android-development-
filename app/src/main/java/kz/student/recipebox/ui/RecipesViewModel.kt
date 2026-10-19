package kz.student.recipebox.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kz.student.recipebox.RecipeBoxApp
import kz.student.recipebox.data.Recipe
import kz.student.recipebox.data.RecipeRepository
import java.io.IOException

const val ALL_CATEGORIES = "Все"

// The "order board" for the catalogue and saved screens. Only val: a new state is made with copy().
// Loading: isLoading. Error: error != null. Empty: recipes is empty. Content: recipes is not empty.
data class RecipesUiState(
 val isLoading: Boolean = true,
 val error: String? = null,
 val searchQuery: String = "",
 val selectedCategory: String = ALL_CATEGORIES,
 val categories: List<String> = listOf(ALL_CATEGORIES),
 val sortByTime: Boolean = false,
 val recipes: List<Recipe> = emptyList(),
 val favoriteIds: Set<Int> = emptySet(),
 val favorites: List<Recipe> = emptyList()
)

// The "chef" for the catalogue and saved screens. One instance lives in MainActivity's
// ViewModelStore, so it survives rotation and keeps search text, category, sorting and favourites.
class RecipesViewModel(private val repository: RecipeRepository) : ViewModel() {
 private val _uiState = MutableStateFlow(RecipesUiState())
 val uiState: StateFlow<RecipesUiState> = _uiState.asStateFlow()

 // Full list from the repository; the UI only gets the prepared copy in uiState.recipes.
 private var allRecipes: List<Recipe> = emptyList()

 init {
  Log.d(TAG, "init: RecipesViewModel created")
  loadRecipes()
  viewModelScope.launch {
   repository.favoriteIds.collect { ids ->
    _uiState.update { it.copy(favoriteIds = ids, favorites = allRecipes.filter { recipe -> recipe.id in ids }) }
   }
  }
 }

 fun onSearchChange(query: String) {
  _uiState.update { it.copy(searchQuery = query, recipes = prepareRecipes(allRecipes, query, it.selectedCategory, it.sortByTime)) }
 }

 fun onCategorySelected(category: String) {
  _uiState.update { it.copy(selectedCategory = category, recipes = prepareRecipes(allRecipes, it.searchQuery, category, it.sortByTime)) }
 }

 fun onSortToggle() {
  _uiState.update {
   val sort = !it.sortByTime
   it.copy(sortByTime = sort, recipes = prepareRecipes(allRecipes, it.searchQuery, it.selectedCategory, sort))
  }
 }

 fun onFavoriteClick(id: Int) {
  repository.toggleFavorite(id)
 }

 fun onRetry() {
  loadRecipes()
 }

 private fun loadRecipes() {
  viewModelScope.launch {
   _uiState.update { it.copy(isLoading = true, error = null) }
   try {
    allRecipes = repository.getRecipes()
    _uiState.update { state ->
     state.copy(
      isLoading = false,
      categories = listOf(ALL_CATEGORIES) + allRecipes.map { it.category }.distinct(),
      recipes = prepareRecipes(allRecipes, state.searchQuery, state.selectedCategory, state.sortByTime),
      favorites = allRecipes.filter { it.id in state.favoriteIds }
     )
    }
   } catch (e: IOException) {
    _uiState.update { it.copy(isLoading = false, error = "Не удалось загрузить рецепты. Проверьте соединение и попробуйте ещё раз.") }
   }
  }
 }

 override fun onCleared() {
  Log.d(TAG, "onCleared: RecipesViewModel destroyed")
  super.onCleared()
 }

 companion object {
  private const val TAG = "RecipesViewModel"

  // Tells Android how to create this ViewModel: take the shared repository from the Application.
  val Factory = viewModelFactory {
   initializer { RecipesViewModel((this[APPLICATION_KEY] as RecipeBoxApp).repository) }
  }
 }
}

// Search, filter and sort live here, not inside a composable.
fun prepareRecipes(recipes: List<Recipe>, query: String, category: String, sortByTime: Boolean): List<Recipe> {
 val text = query.trim()
 val filtered = recipes.filter { recipe ->
  val categoryMatches = category == ALL_CATEGORIES || recipe.category == category
  val textMatches = text.isEmpty() || recipe.title.contains(text, ignoreCase = true) ||
   recipe.ingredients.any { it.contains(text, ignoreCase = true) }
  categoryMatches && textMatches
 }
 return if (sortByTime) filtered.sortedBy { it.cookTimeMinutes } else filtered
}
