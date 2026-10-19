package kz.student.recipebox

import android.app.Application
import kz.student.recipebox.data.RecipeRepository
import kz.student.recipebox.data.SharedPrefsFavoritesStorage

// Created once per app process. Holds the single RecipeRepository that all ViewModels share.
class RecipeBoxApp : Application() {
 val repository: RecipeRepository by lazy {
  RecipeRepository(SharedPrefsFavoritesStorage(this), shouldFail = SIMULATE_SERVER_ERROR)
 }

 companion object {
  // Set to true and run the app again to see the error state and the Retry button.
  const val SIMULATE_SERVER_ERROR = false
 }
}
