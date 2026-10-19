package kz.student.recipebox

import androidx.compose.runtime.Composable
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import kz.student.recipebox.ui.*
import kz.student.recipebox.ui.theme.RecipeBoxTheme
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

// Renders the real screens with fake UiState objects into PNG files (Robolectric + Roborazzi, no emulator).
// GitHub Actions runs `./gradlew recordRoborazziDebug` and copies the images into /screenshots for the README.
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = RobolectricDeviceQualifiers.Pixel5)
class ScreenshotTest {
 private fun shot(name: String, dark: Boolean = false, content: @Composable () -> Unit) {
  captureRoboImage("build/outputs/roborazzi/$name.png") { RecipeBoxTheme(darkTheme = dark) { content() } }
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
