package kz.student.almatyguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import kz.student.almatyguide.data.Places
import kz.student.almatyguide.ui.*
import kz.student.almatyguide.ui.theme.AlmatyTheme

class MainActivity : ComponentActivity() {
 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  enableEdgeToEdge()
  // Small local persistence keeps favorites after rotation and reopening the app.
  val preferences = getSharedPreferences("favorites", MODE_PRIVATE)
  val initialIds = preferences.getStringSet("ids", emptySet()).orEmpty().mapNotNull { it.toIntOrNull() }.toSet()
  setContent {
   AlmatyTheme {
    var favorites by remember { mutableStateOf(initialIds) }
    val navController = rememberNavController()
    val toggle: (Int) -> Unit = { id ->
     favorites = if (id in favorites) favorites - id else favorites + id
     preferences.edit().putStringSet("ids", favorites.map { it.toString() }.toSet()).apply()
    }
    val open: (Int) -> Unit = { id -> navController.navigate("detail/$id") { launchSingleTop = true } }
    NavHost(navController, startDestination = "list") {
     composable("list") {
      ListScreen(Places.items, favorites, open, toggle, { navController.navigate("favorites") { launchSingleTop = true } })
     }
     composable("detail/{id}", arguments = listOf(navArgument("id") { type = NavType.IntType })) { entry ->
      val id = entry.arguments?.getInt("id") ?: -1
      DetailScreen(Places.find(id), id in favorites, { toggle(id) }, { navController.popBackStack() })
     }
     composable("favorites") {
      FavoritesScreen(Places.items, favorites, open, toggle, { navController.popBackStack() })
     }
    }
   }
  }
 }
}
