package de.nilsdruyen.koncept.dogs.ui.favorites

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import de.nilsdruyen.koncept.base.navigation.OnNavigate
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.dogs.ui.components.Loading

@Composable
fun Favorites(
    onNavigate: OnNavigate,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    Favorites(state)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Favorites(state: FavoritesState, modifier: Modifier = Modifier) {
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
        Crossfade(targetState = state, modifier = Modifier.padding(it)) { state ->
            when {
                state.isLoading -> Loading()
                state.list.isEmpty() -> {
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
                    FavoriteList(
                        scrollState = scrollState,
                        list = state.list,
                        showDog = {
                            // TODO: implement
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteList(
    scrollState: LazyListState,
    list: List<Dog>,
    showDog: (Dog) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = PaddingValues(),
        state = scrollState,
        modifier = modifier.fillMaxSize()
    ) {
        items(list) { dog ->
            DogFavoriteItem(dog)
        }
    }
}