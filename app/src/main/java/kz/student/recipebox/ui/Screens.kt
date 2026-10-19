package kz.student.recipebox.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kz.student.recipebox.ui.theme.*

// Screens are stateless "waiters": they get uiState + event lambdas and do not know any ViewModel exists.
// No filtering, sorting or searching here: uiState.recipes is already prepared by the ViewModel.

@Composable
fun RecipesScreen(
 uiState: RecipesUiState,
 onSearchChange: (String) -> Unit,
 onCategorySelected: (String) -> Unit,
 onSortToggle: () -> Unit,
 onRecipeClick: (Int) -> Unit,
 onFavoriteClick: (Int) -> Unit,
 onRetry: () -> Unit,
 onSavedClick: () -> Unit
) {
 Scaffold(topBar = { AppTopBar("RecipeBox", onSaved = onSavedClick) }) { padding ->
  LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(Spacing.md), verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
   item {
    Card(shape = RoundedCornerShape(Spacing.lg), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
     DishImage("Иллюстрация: дымящаяся миска супа", Modifier.fillMaxWidth().height(140.dp))
     Column(Modifier.padding(Spacing.lg), verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
      Text("ДОМАШНЯЯ КУХНЯ", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
      Text("Что приготовим сегодня?", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.onPrimaryContainer)
      Text("Простые рецепты на каждый день и к праздничному столу.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
     }
    }
   }
   item {
    OutlinedTextField(
     value = uiState.searchQuery,
     onValueChange = onSearchChange,
     modifier = Modifier.fillMaxWidth(),
     label = { Text("Блюдо или ингредиент") },
     leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
     singleLine = true
    )
   }
   item {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
     items(uiState.categories) { label -> CategoryChip(label, uiState.selectedCategory == label, { onCategorySelected(label) }) }
    }
   }
   when {
    uiState.isLoading -> item { LoadingState("Загружаем рецепты…") }
    uiState.error != null -> item { ErrorState(uiState.error, onRetry) }
    uiState.recipes.isEmpty() -> item { EmptyState("Ничего не найдено", "Измените запрос или выберите другую категорию.") }
    else -> {
     item {
      Row(verticalAlignment = Alignment.CenterVertically) {
       SectionHeader(if (uiState.sortByTime) "Сначала быстрые" else "Все рецепты", recipesCountLabel(uiState.recipes.size), Modifier.weight(1f))
       TextButton(onClick = onSortToggle, modifier = Modifier.heightIn(min = Spacing.touch)) {
        Text(if (uiState.sortByTime) "Без сортировки" else "По времени")
       }
      }
     }
     items(uiState.recipes, key = { it.id }) { recipe ->
      RecipeCard(recipe, recipe.id in uiState.favoriteIds, { onRecipeClick(recipe.id) }, { onFavoriteClick(recipe.id) })
     }
    }
   }
  }
 }
}

@Composable
fun DetailScreen(uiState: DetailUiState, onFavoriteClick: () -> Unit, onRetry: () -> Unit, onBack: () -> Unit) {
 // Only how the UI looks right now, not data: plain remember is fine here.
 var ingredientsExpanded by remember { mutableStateOf(true) }
 Scaffold(topBar = { AppTopBar("Рецепт", onBack = onBack) }) { padding ->
  LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(Spacing.md), verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
   val recipe = uiState.recipe
   when {
    uiState.isLoading -> item { LoadingState("Загружаем рецепт…") }
    uiState.error != null -> item { ErrorState(uiState.error, onRetry) }
    recipe == null -> item { EmptyState("Рецепт не найден", "Вернитесь назад и выберите рецепт из списка.") }
    else -> {
     item {
      Card(shape = RoundedCornerShape(Spacing.lg)) {
       DishImage("Иллюстрация блюда «${recipe.title}»", Modifier.fillMaxWidth().height(180.dp))
      }
     }
     item {
      Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
       Text(recipe.category.uppercase(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
       Text(recipe.title, style = MaterialTheme.typography.headlineMedium, maxLines = 3, overflow = TextOverflow.Ellipsis)
      }
     }
     item {
      LazyRow(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
       item { InfoBadge(cookTimeLabel(recipe.cookTimeMinutes)) }
       item { InfoBadge("${recipe.servings} порц.") }
       item { InfoBadge(recipe.difficulty) }
      }
     }
     item { Text(recipe.description, style = MaterialTheme.typography.bodyLarge) }
     item {
      Column {
       TextButton(onClick = { ingredientsExpanded = !ingredientsExpanded }, modifier = Modifier.heightIn(min = Spacing.touch)) {
        Text("Ингредиенты (${recipe.ingredients.size})", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.width(Spacing.xs))
        Icon(if (ingredientsExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
         contentDescription = if (ingredientsExpanded) "Свернуть ингредиенты" else "Показать ингредиенты")
       }
       AnimatedVisibility(visible = ingredientsExpanded) {
        Column(Modifier.padding(start = Spacing.md), verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
         recipe.ingredients.forEach { Text("•  $it", style = MaterialTheme.typography.bodyLarge) }
        }
       }
      }
     }
     item { SectionHeader("Приготовление", "Шагов: ${recipe.steps.size}") }
     itemsIndexed(recipe.steps) { index, step -> StepRow(index + 1, step) }
     item {
      Button(onClick = onFavoriteClick, modifier = Modifier.fillMaxWidth().heightIn(min = Spacing.touch)) {
       Text(if (uiState.isFavorite) "Убрать из сохранённых" else "Сохранить рецепт")
      }
     }
    }
   }
  }
 }
}

@Composable
private fun StepRow(number: Int, text: String) {
 Row(horizontalArrangement = Arrangement.spacedBy(Spacing.md), verticalAlignment = Alignment.Top) {
  Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primary, modifier = Modifier.size(Spacing.lg + Spacing.xs)) {
   Box(contentAlignment = Alignment.Center) {
    Text("$number", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onPrimary)
   }
  }
  Text(text, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
 }
}

@Composable
fun SavedScreen(uiState: RecipesUiState, onRecipeClick: (Int) -> Unit, onFavoriteClick: (Int) -> Unit, onRetry: () -> Unit, onBack: () -> Unit) {
 Scaffold(topBar = { AppTopBar("Сохранённые", onBack = onBack) }) { padding ->
  LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(Spacing.md), verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
   when {
    uiState.isLoading -> item { LoadingState("Загружаем сохранённые…") }
    uiState.error != null -> item { ErrorState(uiState.error, onRetry) }
    uiState.favorites.isEmpty() -> item { EmptyState("Пока ничего не сохранено", "Нажмите на сердечко у рецепта, и он появится здесь.") }
    else -> {
     item { SectionHeader("Моя кулинарная книга", recipesCountLabel(uiState.favorites.size)) }
     items(uiState.favorites, key = { it.id }) { recipe -> RecipeCard(recipe, true, { onRecipeClick(recipe.id) }, { onFavoriteClick(recipe.id) }) }
    }
   }
  }
 }
}

// Previews for every screen in every state, built from fake UiState objects in PreviewData.kt.
@Preview(showBackground = true) @Composable private fun RecipesContentPreview() { RecipeBoxTheme { RecipesScreen(previewContentState, {}, {}, {}, {}, {}, {}, {}) } }
@Preview(showBackground = true) @Composable private fun RecipesLoadingPreview() { RecipeBoxTheme { RecipesScreen(previewLoadingState, {}, {}, {}, {}, {}, {}, {}) } }
@Preview(showBackground = true) @Composable private fun RecipesEmptyPreview() { RecipeBoxTheme { RecipesScreen(previewEmptyState, {}, {}, {}, {}, {}, {}, {}) } }
@Preview(showBackground = true) @Composable private fun RecipesErrorPreview() { RecipeBoxTheme { RecipesScreen(previewErrorState, {}, {}, {}, {}, {}, {}, {}) } }
@Preview(showBackground = true) @Composable private fun RecipesDarkPreview() { RecipeBoxTheme(true) { RecipesScreen(previewContentState, {}, {}, {}, {}, {}, {}, {}) } }

@Preview(showBackground = true) @Composable private fun DetailContentPreview() { RecipeBoxTheme { DetailScreen(previewDetailContent, {}, {}, {}) } }
@Preview(showBackground = true) @Composable private fun DetailLoadingPreview() { RecipeBoxTheme { DetailScreen(previewDetailLoading, {}, {}, {}) } }
@Preview(showBackground = true) @Composable private fun DetailNotFoundPreview() { RecipeBoxTheme { DetailScreen(previewDetailNotFound, {}, {}, {}) } }
@Preview(showBackground = true) @Composable private fun DetailErrorPreview() { RecipeBoxTheme { DetailScreen(previewDetailError, {}, {}, {}) } }
@Preview(showBackground = true) @Composable private fun DetailDarkPreview() { RecipeBoxTheme(true) { DetailScreen(previewDetailContent, {}, {}, {}) } }

@Preview(showBackground = true) @Composable private fun SavedContentPreview() { RecipeBoxTheme { SavedScreen(previewContentState, {}, {}, {}, {}) } }
@Preview(showBackground = true) @Composable private fun SavedLoadingPreview() { RecipeBoxTheme { SavedScreen(previewLoadingState, {}, {}, {}, {}) } }
@Preview(showBackground = true) @Composable private fun SavedEmptyPreview() { RecipeBoxTheme { SavedScreen(previewEmptyState, {}, {}, {}, {}) } }
@Preview(showBackground = true) @Composable private fun SavedErrorPreview() { RecipeBoxTheme { SavedScreen(previewErrorState, {}, {}, {}, {}) } }
@Preview(showBackground = true) @Composable private fun SavedDarkPreview() { RecipeBoxTheme(true) { SavedScreen(previewContentState, {}, {}, {}, {}) } }
