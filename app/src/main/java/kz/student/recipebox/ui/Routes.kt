package kz.student.recipebox.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

// Routes are the only composables that know about ViewModels.
// They read state with collectAsStateWithLifecycle() and pass ViewModel functions down as event lambdas.

@Composable
fun RecipesRoute(viewModel: RecipesViewModel, onRecipeClick: (Int) -> Unit, onSavedClick: () -> Unit) {
 val uiState by viewModel.uiState.collectAsStateWithLifecycle()
 RecipesScreen(
  uiState = uiState,
  onSearchChange = viewModel::onSearchChange,
  onCategorySelected = viewModel::onCategorySelected,
  onSortToggle = viewModel::onSortToggle,
  onRecipeClick = onRecipeClick,
  onFavoriteClick = viewModel::onFavoriteClick,
  onRetry = viewModel::onRetry,
  onSavedClick = onSavedClick
 )
}

@Composable
fun SavedRoute(viewModel: RecipesViewModel, onRecipeClick: (Int) -> Unit, onBack: () -> Unit) {
 val uiState by viewModel.uiState.collectAsStateWithLifecycle()
 SavedScreen(
  uiState = uiState,
  onRecipeClick = onRecipeClick,
  onFavoriteClick = viewModel::onFavoriteClick,
  onRetry = viewModel::onRetry,
  onBack = onBack
 )
}

// viewModel() here is scoped to this navigation entry, so each opened recipe gets its own DetailViewModel,
// and the "id" navigation argument reaches it through SavedStateHandle.
@Composable
fun DetailRoute(onBack: () -> Unit, viewModel: DetailViewModel = viewModel(factory = DetailViewModel.Factory)) {
 val uiState by viewModel.uiState.collectAsStateWithLifecycle()
 DetailScreen(
  uiState = uiState,
  onFavoriteClick = viewModel::onFavoriteClick,
  onRetry = viewModel::onRetry,
  onBack = onBack
 )
}
