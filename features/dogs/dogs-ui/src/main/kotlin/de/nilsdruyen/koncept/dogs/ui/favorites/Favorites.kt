package de.nilsdruyen.koncept.dogs.ui.favorites

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.primarySurface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.dogs.ui.components.Loading

@Composable
fun Favorites(viewModel: FavoritesViewModel) {
    val state by viewModel.state.collectAsState()

    Favorites(state)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Favorites(state: FavoritesState, modifier: Modifier = Modifier) {
    val scrollState = rememberLazyListState()
    val appBarScrollState = rememberTopAppBarScrollState()
    val scrollBehavior = remember {
        TopAppBarDefaults.pinnedScrollBehavior(appBarScrollState)
    }
    val color =
        TopAppBarDefaults.smallTopAppBarColors().containerColor(scrollFraction = scrollBehavior.scrollFraction).value

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SmallTopAppBar(
                title = { Text("Favoriten") },
                modifier = Modifier
                    .background(color)
                    .statusBarsPadding(),
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        Crossfade(targetState = state) { state ->
            when {
                state.isLoading -> Loading(Modifier.padding(it))
                state.list.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                    ) {
                        Text(
                            text = "No favorites!",
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
                else -> FavoriteList(
                    scrollState = scrollState,
                    list = state.list,
                    showDog = {

                    },
                    modifier = Modifier.padding(it),
                )
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
        contentPadding = PaddingValues(bottom = 4.dp),
        state = scrollState,
        modifier = modifier
            .fillMaxSize()
    ) {
        items(list) { dog ->
            DogFavoriteItem(dog)
        }
    }
}