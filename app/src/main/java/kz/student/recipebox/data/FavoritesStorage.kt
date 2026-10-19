package kz.student.recipebox.data

import android.content.Context

// Where saved recipe ids are kept between app launches.
// It is an interface so unit tests can use a simple in-memory version instead of SharedPreferences.
interface FavoritesStorage {
 fun load(): Set<Int>
 fun save(ids: Set<Int>)
}

class SharedPrefsFavoritesStorage(context: Context) : FavoritesStorage {
 private val prefs = context.getSharedPreferences("saved_recipes", Context.MODE_PRIVATE)

 override fun load(): Set<Int> =
  prefs.getStringSet("ids", emptySet()).orEmpty().mapNotNull { it.toIntOrNull() }.toSet()

 override fun save(ids: Set<Int>) {
  prefs.edit().putStringSet("ids", ids.map { it.toString() }.toSet()).apply()
 }
}
