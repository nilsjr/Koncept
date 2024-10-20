package de.nilsdruyen.koncept.dogs.ui.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.nilsdruyen.koncept.dogs.entity.BreedId
import de.nilsdruyen.koncept.dogs.ui.components.Loading

@Composable
fun Favorites(
    showBreed: (Int) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.showBreed) {
        state.showBreed?.let {
            showBreed(it)
            viewModel.sendIntent(FavoritesIntent.NavigationConsumed)
        }
    }

    Favorites(
        state = state,
        showBreed = {
            viewModel.sendIntent(FavoritesIntent.ShowBreed(it.value))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Favorites(
    state: FavoritesState,
    showBreed: (BreedId) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()
    val appBarScrollState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(appBarScrollState)

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text("Favoriten") },
                modifier = Modifier
                    .statusBarsPadding()
                    .testTag("fav_appbar"),
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when {
                state.isLoading -> Loading()
                state.list.items.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Text(
                            text = "No favorites!",
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.Center)
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(),
                        state = scrollState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.list.items) { dog ->
                            DogFavoriteItem(dog, Modifier.fillMaxWidth(), showBreed)
                        }
                    }
                }
            }
        }
    }
}
