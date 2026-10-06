package kz.student.almatyguide.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kz.student.almatyguide.R
import kz.student.almatyguide.data.Place
import kz.student.almatyguide.data.Places
import kz.student.almatyguide.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(title: String, onBack: (() -> Unit)? = null, onFavorites: (() -> Unit)? = null) {
 TopAppBar(
  title = { Text(title, style = MaterialTheme.typography.titleLarge, maxLines = 1, overflow = TextOverflow.Ellipsis) },
  navigationIcon = { onBack?.let { IconButton(onClick = it, modifier = Modifier.size(Spacing.touch)) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад") } } },
  actions = { onFavorites?.let { IconButton(onClick = it, modifier = Modifier.size(Spacing.touch)) { Icon(Icons.Default.FavoriteBorder, "Открыть избранное") } } },
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
fun TagChip(label: String, selected: Boolean, onClick: (() -> Unit)?, modifier: Modifier = Modifier) {
 if (onClick == null) {
  Surface(modifier = modifier.heightIn(min = Spacing.touch), shape = RoundedCornerShape(Spacing.sm), color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant) {
   Box(Modifier.padding(horizontal = Spacing.md, vertical = Spacing.sm), contentAlignment = Alignment.Center) {
    Text(label, style = MaterialTheme.typography.labelLarge, maxLines = 1, overflow = TextOverflow.Ellipsis)
   }
  }
 } else FilterChip(selected = selected, onClick = onClick, modifier = modifier.heightIn(min = Spacing.touch),
  label = { Text(label, style = MaterialTheme.typography.labelLarge, maxLines = 1, overflow = TextOverflow.Ellipsis) })
}
@Composable
fun ScenicImage(description: String, modifier: Modifier = Modifier) {
 Image(painter = painterResource(R.drawable.mountains), contentDescription = description,
  modifier = modifier.fillMaxWidth(), contentScale = ContentScale.Crop)
}
@Composable
fun PlaceCard(place: Place, favorite: Boolean, onOpen: () -> Unit, onToggle: () -> Unit, modifier: Modifier = Modifier) {
 Card(onClick = onOpen, modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(Spacing.md),
  colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
  Row(Modifier.padding(Spacing.md), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
   Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
    Text(place.category.uppercase(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
    Text(place.name, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
    Text(place.subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
   }
   IconButton(onClick = onToggle, modifier = Modifier.size(Spacing.touch)) {
    Icon(if (favorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
     if (favorite) "Убрать ${place.name} из избранного" else "Добавить ${place.name} в избранное", tint = MaterialTheme.colorScheme.primary)
   }
  }
 }
}
@Composable
fun EmptyState(title: String, message: String, modifier: Modifier = Modifier) {
 Column(modifier.fillMaxWidth().padding(Spacing.lg), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
  Icon(Icons.Default.FavoriteBorder, contentDescription = null, modifier = Modifier.size(Spacing.touch), tint = MaterialTheme.colorScheme.primary)
  Text(title, style = MaterialTheme.typography.titleLarge)
  Text(message, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
 }
}
@Preview @Composable private fun TopBarPreview() { AlmatyTheme { AppTopBar("Алматы", onBack = {}) } }
@Preview @Composable private fun HeaderPreview() { AlmatyTheme { SectionHeader("Места", "12 идей для прогулки") } }
@Preview @Composable private fun TagPreview() { AlmatyTheme { TagChip("Природа", true, {}) } }
@Preview @Composable private fun ImagePreview() { AlmatyTheme { ScenicImage("Иллюстрация гор Алматы", Modifier.height(180.dp)) } }
@Preview @Composable private fun CardPreview() { AlmatyTheme { PlaceCard(Places.items[2], true, {}, {}) } }
@Preview @Composable private fun EmptyPreview() { AlmatyTheme { EmptyState("Пока пусто", "Нажмите на сердечко у места") } }
@Preview @Composable private fun CardDarkPreview() { AlmatyTheme(darkTheme = true) { PlaceCard(Places.items[0], true, {}, {}) } }
