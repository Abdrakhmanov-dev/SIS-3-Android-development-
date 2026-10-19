package kz.student.recipebox

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kz.student.recipebox.data.FavoritesStorage
import kz.student.recipebox.data.RecipeRepository
import kz.student.recipebox.ui.RecipesViewModel
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecipesViewModelTest {
 private val dispatcher = StandardTestDispatcher()

 // In-memory storage, so the test does not need Android SharedPreferences.
 private class FakeFavoritesStorage : FavoritesStorage {
  var saved: Set<Int> = emptySet()
  override fun load(): Set<Int> = saved
  override fun save(ids: Set<Int>) { saved = ids }
 }

 private fun createViewModel(storage: FavoritesStorage = FakeFavoritesStorage(), shouldFail: Boolean = false) =
  RecipesViewModel(RecipeRepository(storage, shouldFail))

 @Before fun setUp() { Dispatchers.setMain(dispatcher) }
 @After fun tearDown() { Dispatchers.resetMain() }

 @Test
 fun afterLoading_showsAllTwelveRecipes() = runTest(dispatcher) {
  val viewModel = createViewModel()
  assertTrue(viewModel.uiState.value.isLoading)

  advanceUntilIdle() // skips the repository's delay()

  assertFalse(viewModel.uiState.value.isLoading)
  assertEquals(12, viewModel.uiState.value.recipes.size)
 }

 @Test
 fun onCategorySelected_keepsOnlyThatCategory() = runTest(dispatcher) {
  val viewModel = createViewModel()
  advanceUntilIdle()

  viewModel.onCategorySelected("Супы")

  val state = viewModel.uiState.value
  assertEquals("Супы", state.selectedCategory)
  assertEquals(3, state.recipes.size)
  assertTrue(state.recipes.all { it.category == "Супы" })
 }

 @Test
 fun onSearchChange_findsByIngredient() = runTest(dispatcher) {
  val viewModel = createViewModel()
  advanceUntilIdle()

  viewModel.onSearchChange("творог")

  assertEquals(listOf("Сырники"), viewModel.uiState.value.recipes.map { it.title })
 }

 @Test
 fun onSearchChange_withNoMatches_givesEmptyList() = runTest(dispatcher) {
  val viewModel = createViewModel()
  advanceUntilIdle()

  viewModel.onSearchChange("Пицца")

  assertTrue(viewModel.uiState.value.recipes.isEmpty())
 }

 @Test
 fun onSortToggle_putsFastestRecipeFirst() = runTest(dispatcher) {
  val viewModel = createViewModel()
  advanceUntilIdle()

  viewModel.onSortToggle()

  val times = viewModel.uiState.value.recipes.map { it.cookTimeMinutes }
  assertEquals(times.sorted(), times)
  assertEquals("Овсянка с ягодами", viewModel.uiState.value.recipes.first().title)
 }

 @Test
 fun shouldFail_showsErrorState() = runTest(dispatcher) {
  val viewModel = createViewModel(shouldFail = true)
  advanceUntilIdle()

  val state = viewModel.uiState.value
  assertFalse(state.isLoading)
  assertNotNull(state.error)
 }

 @Test
 fun onFavoriteClick_savesRecipe() = runTest(dispatcher) {
  val storage = FakeFavoritesStorage()
  val viewModel = createViewModel(storage)
  advanceUntilIdle()

  viewModel.onFavoriteClick(5)
  advanceUntilIdle()

  assertEquals(setOf(5), viewModel.uiState.value.favoriteIds)
  assertEquals("Сырники", viewModel.uiState.value.favorites.single().title)
  assertEquals(setOf(5), storage.saved)
 }
}
