package de.nilsdruyen.koncept.dogs.ui.favorites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun Favorites(viewModel: FavoritesViewModel) {
    val state by viewModel.state.collectAsState()

    Favorites(state)
}

@Composable
fun Favorites(state: FavoritesState) {

}