package kz.student.recipebox

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kz.student.recipebox.ui.DetailRoute
import kz.student.recipebox.ui.RecipesRoute
import kz.student.recipebox.ui.RecipesViewModel
import kz.student.recipebox.ui.SavedRoute
import kz.student.recipebox.ui.theme.RecipeBoxTheme

class MainActivity : ComponentActivity() {
 // Activity-scoped: shared by the catalogue and saved screens and kept across rotation.
 private val recipesViewModel: RecipesViewModel by viewModels { RecipesViewModel.Factory }

 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  Log.d(TAG, "onCreate (savedInstanceState = ${if (savedInstanceState == null) "null" else "present"})")
  enableEdgeToEdge()
  setContent { RecipeBoxTheme { RecipeBoxNavHost(recipesViewModel) } }
 }

 override fun onStart() { super.onStart(); Log.d(TAG, "onStart") }
 override fun onResume() { super.onResume(); Log.d(TAG, "onResume") }
 override fun onPause() { super.onPause(); Log.d(TAG, "onPause") }
 override fun onStop() { super.onStop(); Log.d(TAG, "onStop") }
 override fun onDestroy() { super.onDestroy(); Log.d(TAG, "onDestroy (isChangingConfigurations = $isChangingConfigurations)") }

 private companion object {
  const val TAG = "MainActivity"
 }
}

private const val ROUTE_RECIPES = "recipes"
private const val ROUTE_SAVED = "saved"
private const val ROUTE_DETAIL = "detail/{id}"

@Composable
private fun RecipeBoxNavHost(recipesViewModel: RecipesViewModel) {
 val navController = rememberNavController()
 val backStackEntry by navController.currentBackStackEntryAsState()
 val currentRoute = backStackEntry?.destination?.route
 val openRecipe: (Int) -> Unit = { id -> navController.navigate("detail/$id") { launchSingleTop = true } }

 Scaffold(
  bottomBar = {
   // Bottom navigation between the two main sections; hidden on the recipe screen.
   if (currentRoute != ROUTE_DETAIL) {
    NavigationBar {
     NavigationBarItem(selected = currentRoute == ROUTE_RECIPES, onClick = { navController.openTab(ROUTE_RECIPES) },
      icon = { Icon(Icons.Default.Home, contentDescription = null) }, label = { Text("Рецепты") })
     NavigationBarItem(selected = currentRoute == ROUTE_SAVED, onClick = { navController.openTab(ROUTE_SAVED) },
      icon = { Icon(Icons.Default.Favorite, contentDescription = null) }, label = { Text("Сохранённые") })
    }
   }
  }
 ) { padding ->
  NavHost(navController, startDestination = ROUTE_RECIPES, modifier = Modifier.padding(padding).consumeWindowInsets(padding)) {
   composable(ROUTE_RECIPES) {
    RecipesRoute(recipesViewModel, onRecipeClick = openRecipe, onSavedClick = { navController.openTab(ROUTE_SAVED) })
   }
   composable(ROUTE_SAVED) {
    SavedRoute(recipesViewModel, onRecipeClick = openRecipe, onBack = { navController.popBackStack() })
   }
   composable(ROUTE_DETAIL, arguments = listOf(navArgument("id") { type = NavType.IntType })) {
    DetailRoute(onBack = { navController.popBackStack() })
   }
  }
 }
}

// Switch tabs without piling up copies of the same screen in the back stack.
private fun NavHostController.openTab(route: String) {
 navigate(route) {
  popUpTo(graph.findStartDestination().id) { saveState = true }
  launchSingleTop = true
  restoreState = true
 }
}
