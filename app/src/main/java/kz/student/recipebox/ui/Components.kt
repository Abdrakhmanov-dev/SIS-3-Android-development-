package kz.student.recipebox.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kz.student.recipebox.R
import kz.student.recipebox.data.Recipe
import kz.student.recipebox.ui.theme.*

// Reusable building blocks. All colors come from MaterialTheme.colorScheme,
// all text styles from MaterialTheme.typography, all spacing from Spacing.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(title: String, onBack: (() -> Unit)? = null, onSaved: (() -> Unit)? = null) {
 TopAppBar(
  title = { Text(title, style = MaterialTheme.typography.titleLarge, maxLines = 1, overflow = TextOverflow.Ellipsis) },
  navigationIcon = { onBack?.let { IconButton(onClick = it, modifier = Modifier.size(Spacing.touch)) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад") } } },
  actions = { onSaved?.let { IconButton(onClick = it, modifier = Modifier.size(Spacing.touch)) { Icon(Icons.Default.FavoriteBorder, "Открыть сохранённые рецепты") } } },
  colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
 )
}

@Composable
fun SectionHeader(title: String, subtitle: String, modifier: Modifier = Modifier) {
 Column(modifier, verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
  Text(title, style = MaterialTheme.typography.titleLarge, maxLines = 2, overflow = TextOverflow.Ellipsis)
  Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
 }
}

@Composable
fun CategoryChip(label: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
 FilterChip(
  selected = selected, onClick = onClick, modifier = modifier.heightIn(min = Spacing.touch),
  label = { Text(label, style = MaterialTheme.typography.labelLarge, maxLines = 1, overflow = TextOverflow.Ellipsis) }
 )
}

// A small non-clickable label: cooking time, servings, difficulty.
@Composable
fun InfoBadge(text: String, modifier: Modifier = Modifier) {
 Surface(modifier = modifier, shape = RoundedCornerShape(Spacing.sm), color = MaterialTheme.colorScheme.secondaryContainer) {
  Text(text, modifier = Modifier.padding(horizontal = Spacing.sm, vertical = Spacing.xs),
   style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSecondaryContainer, maxLines = 1)
 }
}

// Image from resources (res/drawable/dish.xml) with a meaningful description for screen readers.
@Composable
fun DishImage(description: String, modifier: Modifier = Modifier) {
 Image(painter = painterResource(R.drawable.dish), contentDescription = description, modifier = modifier, contentScale = ContentScale.Crop)
}

@Composable
fun RecipeCard(recipe: Recipe, saved: Boolean, onOpen: () -> Unit, onToggleSaved: () -> Unit, modifier: Modifier = Modifier) {
 Card(onClick = onOpen, modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(Spacing.md),
  colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)) {
  Row(Modifier.padding(Spacing.sm), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.md)) {
   DishImage("Иллюстрация блюда «${recipe.title}»", Modifier.size(width = 72.dp, height = 64.dp).clip(RoundedCornerShape(Spacing.sm)))
   Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
    Text(recipe.category.uppercase(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
    Text(recipe.title, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
    Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
     InfoBadge(cookTimeLabel(recipe.cookTimeMinutes))
     InfoBadge(recipe.difficulty)
    }
   }
   IconButton(onClick = onToggleSaved, modifier = Modifier.size(Spacing.touch)) {
    Icon(if (saved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
     if (saved) "Убрать «${recipe.title}» из сохранённых" else "Сохранить «${recipe.title}»", tint = MaterialTheme.colorScheme.primary)
   }
  }
 }
}

@Composable
fun EmptyState(title: String, message: String, modifier: Modifier = Modifier) {
 Column(modifier.fillMaxWidth().padding(Spacing.lg), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
  Icon(Icons.Default.FavoriteBorder, contentDescription = null, modifier = Modifier.size(Spacing.touch), tint = MaterialTheme.colorScheme.primary)
  Text(title, style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
  Text(message, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
 }
}

// Shown while the repository is "talking to the server" (delay()).
@Composable
fun LoadingState(message: String, modifier: Modifier = Modifier) {
 Column(modifier.fillMaxWidth().padding(Spacing.lg), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
  CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
  Text(message, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
 }
}

// Shown when the repository throws an error; the button sends the onRetry event up to the ViewModel.
@Composable
fun ErrorState(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
 Column(modifier.fillMaxWidth().padding(Spacing.lg), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
  Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(Spacing.touch), tint = MaterialTheme.colorScheme.error)
  Text("Что-то пошло не так", style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
  Text(message, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
  Button(onClick = onRetry, modifier = Modifier.heightIn(min = Spacing.touch)) {
   Icon(Icons.Default.Refresh, contentDescription = null)
   Spacer(Modifier.width(Spacing.sm))
   Text("Повторить")
  }
 }
}

// Text formatting for the UI (not business logic): 25 -> "25 мин", 90 -> "1 ч 30 мин".
fun cookTimeLabel(minutes: Int): String {
 val h = minutes / 60
 val m = minutes % 60
 return when {
  h == 0 -> "$m мин"
  m == 0 -> "$h ч"
  else -> "$h ч $m мин"
 }
}

// Russian plural for the count line: 1 рецепт, 3 рецепта, 12 рецептов.
fun recipesCountLabel(count: Int): String {
 val mod100 = count % 100
 val mod10 = count % 10
 val word = when {
  mod100 in 11..14 -> "рецептов"
  mod10 == 1 -> "рецепт"
  mod10 in 2..4 -> "рецепта"
  else -> "рецептов"
 }
 return "$count $word"
}

@Preview @Composable private fun TopBarPreview() { RecipeBoxTheme { AppTopBar("Рецепт", onBack = {}) } }
@Preview(showBackground = true) @Composable private fun HeaderPreview() { RecipeBoxTheme { SectionHeader("Что приготовим?", "12 рецептов") } }
@Preview @Composable private fun ChipPreview() { RecipeBoxTheme { CategoryChip("Супы", true, {}) } }
@Preview @Composable private fun BadgePreview() { RecipeBoxTheme { InfoBadge("1 ч 30 мин") } }
@Preview @Composable private fun ImagePreview() { RecipeBoxTheme { DishImage("Иллюстрация блюда", Modifier.fillMaxWidth().height(140.dp)) } }
@Preview @Composable private fun CardPreview() { RecipeBoxTheme { RecipeCard(previewRecipes[1], true, {}, {}) } }
@Preview @Composable private fun CardDarkPreview() { RecipeBoxTheme(darkTheme = true) { RecipeCard(previewRecipes[0], false, {}, {}) } }
@Preview(showBackground = true) @Composable private fun EmptyPreview() { RecipeBoxTheme { EmptyState("Пока пусто", "Нажмите на сердечко у рецепта") } }
@Preview(showBackground = true) @Composable private fun LoadingPreview() { RecipeBoxTheme { LoadingState("Загружаем рецепты…") } }
@Preview(showBackground = true) @Composable private fun ErrorPreview() { RecipeBoxTheme { ErrorState("Не удалось загрузить рецепты.", {}) } }
