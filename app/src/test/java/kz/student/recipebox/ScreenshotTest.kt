package kz.student.recipebox

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import androidx.compose.runtime.Composable
import kz.student.recipebox.ui.*
import kz.student.recipebox.ui.theme.RecipeBoxTheme
import org.junit.Rule
import org.junit.Test

// Renders the real screens with fake UiState objects into PNG files (Paparazzi, no emulator needed).
// GitHub Actions runs `./gradlew recordPaparazziDebug` and copies the images into /screenshots for the README.
class ScreenshotTest {
 @get:Rule
 val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

 private fun shot(name: String, dark: Boolean = false, content: @Composable () -> Unit) {
  paparazzi.snapshot(name) { RecipeBoxTheme(darkTheme = dark) { content() } }
 }

 @Test fun recipesLight() = shot("recipes-light") { RecipesScreen(previewContentState, {}, {}, {}, {}, {}, {}, {}) }
 @Test fun recipesDark() = shot("recipes-dark", dark = true) { RecipesScreen(previewContentState, {}, {}, {}, {}, {}, {}, {}) }
 @Test fun recipesLoading() = shot("recipes-loading") { RecipesScreen(previewLoadingState, {}, {}, {}, {}, {}, {}, {}) }
 @Test fun recipesEmpty() = shot("recipes-empty") { RecipesScreen(previewEmptyState, {}, {}, {}, {}, {}, {}, {}) }
 @Test fun recipesError() = shot("recipes-error") { RecipesScreen(previewErrorState, {}, {}, {}, {}, {}, {}, {}) }

 @Test fun detailLight() = shot("detail-light") { DetailScreen(previewDetailContent, {}, {}, {}) }
 @Test fun detailDark() = shot("detail-dark", dark = true) { DetailScreen(previewDetailContent, {}, {}, {}) }

 @Test fun savedLight() = shot("saved-light") { SavedScreen(previewContentState, {}, {}, {}, {}) }
 @Test fun savedDark() = shot("saved-dark", dark = true) { SavedScreen(previewContentState, {}, {}, {}, {}) }
 @Test fun savedEmpty() = shot("saved-empty") { SavedScreen(previewEmptyState, {}, {}, {}, {}) }
}
