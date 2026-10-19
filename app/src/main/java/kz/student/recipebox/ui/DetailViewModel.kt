package kz.student.recipebox.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
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

// recipe == null after loading (and without error) means "no recipe with this id".
data class DetailUiState(
 val isLoading: Boolean = true,
 val error: String? = null,
 val recipe: Recipe? = null,
 val isFavorite: Boolean = false
)

// One DetailViewModel per opened recipe. Navigation puts the "id" argument into SavedStateHandle,
// so the ViewModel reads it itself; the screen never passes the id around.
class DetailViewModel(savedStateHandle: SavedStateHandle, private val repository: RecipeRepository) : ViewModel() {
 private val recipeId: Int = savedStateHandle.get<Int>("id") ?: -1

 private val _uiState = MutableStateFlow(DetailUiState())
 val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

 init {
  Log.d(TAG, "init: DetailViewModel created for id=$recipeId")
  loadRecipe()
  viewModelScope.launch {
   repository.favoriteIds.collect { ids -> _uiState.update { it.copy(isFavorite = recipeId in ids) } }
  }
 }

 fun onFavoriteClick() {
  repository.toggleFavorite(recipeId)
 }

 fun onRetry() {
  loadRecipe()
 }

 private fun loadRecipe() {
  viewModelScope.launch {
   _uiState.update { it.copy(isLoading = true, error = null) }
   try {
    val recipe = repository.getRecipe(recipeId)
    _uiState.update { it.copy(isLoading = false, recipe = recipe) }
   } catch (e: IOException) {
    _uiState.update { it.copy(isLoading = false, error = "Не удалось загрузить рецепт. Попробуйте ещё раз.") }
   }
  }
 }

 override fun onCleared() {
  Log.d(TAG, "onCleared: DetailViewModel destroyed for id=$recipeId")
  super.onCleared()
 }

 companion object {
  private const val TAG = "DetailViewModel"

  val Factory = viewModelFactory {
   initializer { DetailViewModel(createSavedStateHandle(), (this[APPLICATION_KEY] as RecipeBoxApp).repository) }
  }
 }
}
