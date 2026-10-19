package kz.student.recipebox.ui

import kz.student.recipebox.data.Recipe

// Fake data only for @Preview and screenshot tests. Previews never touch the repository or a ViewModel:
// they build a UiState by hand, so every state can be seen without running the app.
internal val previewRecipes = listOf(
 Recipe(5, "Сырники", "Завтраки", 25, 3, "Легко", "Нежные творожные лепёшки с золотистой корочкой.",
  listOf("Творог — 400 г", "Яйцо — 1 шт.", "Мука — 3 ст. л.", "Сахар — 2 ст. л."),
  listOf("Разомните творог с яйцом и сахаром.", "Добавьте муку и сформируйте лепёшки.", "Обжарьте по 3 минуты с каждой стороны.")),
 Recipe(4, "Домашний лагман с тянутой лапшой и говядиной", "Супы", 100, 4, "Сложно", "Густой суп-соус с овощами и длинной лапшой.",
  listOf("Говядина — 400 г", "Лапша — 400 г"), listOf("Обжарьте мясо.", "Добавьте овощи.")),
 Recipe(1, "Бешбармак", "Основные", 180, 6, "Сложно", "Праздничное блюдо из мяса и теста.",
  listOf("Мясо — 1,5 кг", "Мука — 400 г"), listOf("Отварите мясо.", "Отварите тесто."))
)

internal val previewContentState = RecipesUiState(
 isLoading = false,
 categories = listOf(ALL_CATEGORIES, "Основные", "Выпечка", "Супы", "Завтраки", "Салаты"),
 recipes = previewRecipes,
 favoriteIds = setOf(5, 1),
 favorites = listOf(previewRecipes[0], previewRecipes[2])
)
internal val previewLoadingState = RecipesUiState(isLoading = true)
internal val previewEmptyState = previewContentState.copy(searchQuery = "Пицца", recipes = emptyList(), favoriteIds = emptySet(), favorites = emptyList())
internal val previewErrorState = RecipesUiState(isLoading = false, error = "Не удалось загрузить рецепты. Проверьте соединение и попробуйте ещё раз.")

internal val previewDetailContent = DetailUiState(isLoading = false, recipe = previewRecipes[0], isFavorite = true)
internal val previewDetailLoading = DetailUiState(isLoading = true)
internal val previewDetailNotFound = DetailUiState(isLoading = false, recipe = null)
internal val previewDetailError = DetailUiState(isLoading = false, error = "Не удалось загрузить рецепт. Попробуйте ещё раз.")
