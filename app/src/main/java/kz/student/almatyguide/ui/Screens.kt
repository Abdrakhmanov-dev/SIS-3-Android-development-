package kz.student.almatyguide.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kz.student.almatyguide.data.*
import kz.student.almatyguide.ui.theme.*

@Composable
fun ListScreen(places: List<Place>, favorites: Set<Int>, onOpen: (Int) -> Unit, onToggle: (Int) -> Unit, onFavorites: () -> Unit) {
 // The assignment explicitly asks for remember + mutableStateOf.
 var category by remember { mutableStateOf("Все") }
 val visible = places.filter { category == "Все" || it.category == category }
 Scaffold(topBar = { AppTopBar("Almaty Guide", onFavorites = onFavorites) }) { padding ->
  LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(Spacing.md), verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
   item {
    Card(shape = RoundedCornerShape(Spacing.lg), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
     ScenicImage("Авторская иллюстрация вершин Заилийского Алатау", Modifier.height(160.dp))
     Column(Modifier.padding(Spacing.lg), verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
      Text("ГОРОД У ПОДНОЖИЯ ГОР", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
      Text("Открой свой\nАлматы", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.onPrimaryContainer)
      Text("Небольшие открытия для большой прогулки.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
     }
    }
   }
   item { LazyRow(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
    items(Places.categories) { label -> TagChip(label, category == label, { category = label }) }
   } }
   item { SectionHeader("Куда отправимся?", "${visible.size} мест • выбирай и сохраняй") }
   if (visible.isEmpty()) item { EmptyState("Места не найдены", "Выберите другую категорию или добавьте места в список.") }
   items(visible, key = { it.id }) { place -> PlaceCard(place, place.id in favorites, { onOpen(place.id) }, { onToggle(place.id) }) }
  }
 }
}
@Composable
fun DetailScreen(place: Place?, favorite: Boolean, onToggle: () -> Unit, onBack: () -> Unit) {
 var expanded by remember { mutableStateOf(false) }
 Scaffold(topBar = { AppTopBar("О месте", onBack = onBack) }) { padding ->
  LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(Spacing.md), verticalArrangement = Arrangement.spacedBy(Spacing.lg)) {
   if (place == null) {
    item { EmptyState("Место не найдено", "Вернитесь назад и выберите место из списка.") }
   } else {
    item { Card(shape = RoundedCornerShape(Spacing.lg)) {
     ScenicImage("Стилизованный горный пейзаж Алматы; иллюстрация, а не фотография ${place.name}", Modifier.height(220.dp))
    } }
    item { Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
     Text(place.name, style = MaterialTheme.typography.headlineMedium, maxLines = 3, overflow = TextOverflow.Ellipsis)
     Text(place.subtitle, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    } }
    item { LazyRow(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
     item { TagChip(place.category, true, null) }
     item { TagChip("Алматы", false, null) }
    } }
    item { SectionHeader("История места", "Идея для следующего выходного") }
    item { Text(place.description, style = MaterialTheme.typography.bodyLarge) }
    item { Button(onClick = onToggle, modifier = Modifier.fillMaxWidth().heightIn(min = Spacing.touch)) {
     Text(if (favorite) "Убрать из избранного" else "Сохранить в избранное")
    } }
    item {
     TextButton(onClick = { expanded = !expanded }, modifier = Modifier.heightIn(min = Spacing.touch)) { Text(if (expanded) "Скрыть совет" else "Совет перед прогулкой") }
     androidx.compose.animation.AnimatedVisibility(visible = expanded) {
      Text("Проверьте погоду и условия посещения. Возьмите воду и удобную обувь.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
     }
    }
   }
  }
 }
}
@Composable
fun FavoritesScreen(places: List<Place>, favorites: Set<Int>, onOpen: (Int) -> Unit, onToggle: (Int) -> Unit, onBack: () -> Unit) {
 val saved = places.filter { it.id in favorites }
 Scaffold(topBar = { AppTopBar("Избранное", onBack = onBack) }) { padding ->
  LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(Spacing.md), verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
   item { SectionHeader("Твои планы", "${saved.size} мест сохранено для будущих прогулок") }
   if (saved.isEmpty()) item { EmptyState("Первое открытие впереди", "Нажмите на сердечко в каталоге, и любимые места появятся здесь.") }
   items(saved, key = { it.id }) { place -> PlaceCard(place, true, { onOpen(place.id) }, { onToggle(place.id) }) }
  }
 }
}
@Preview(showBackground = true) @Composable private fun ListPreview() { AlmatyTheme { ListScreen(Places.items, emptySet(), {}, {}, {}) } }
@Preview(showBackground = true) @Composable private fun DetailPreview() { AlmatyTheme { DetailScreen(Places.items[0], false, {}, {}) } }
@Preview(showBackground = true) @Composable private fun FavoritesPreview() { AlmatyTheme { FavoritesScreen(Places.items, setOf(1, 3), {}, {}, {}) } }
@Preview(showBackground = true) @Composable private fun EmptyFavoritesPreview() { AlmatyTheme { FavoritesScreen(Places.items, emptySet(), {}, {}, {}) } }
@Preview(showBackground = true) @Composable private fun ListDarkPreview() { AlmatyTheme(true) { ListScreen(Places.items, emptySet(), {}, {}, {}) } }
@Preview(showBackground = true) @Composable private fun DetailDarkPreview() { AlmatyTheme(true) { DetailScreen(Places.items[0], true, {}, {}) } }
@Preview(showBackground = true) @Composable private fun FavoritesDarkPreview() { AlmatyTheme(true) { FavoritesScreen(Places.items, setOf(1), {}, {}, {}) } }
