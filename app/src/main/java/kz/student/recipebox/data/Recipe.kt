package kz.student.recipebox.data

// One recipe. Only val properties: a Recipe never changes after it is created.
data class Recipe(
 val id: Int,
 val title: String,
 val category: String,
 val cookTimeMinutes: Int,
 val servings: Int,
 val difficulty: String,
 val description: String,
 val ingredients: List<String>,
 val steps: List<String>
)
