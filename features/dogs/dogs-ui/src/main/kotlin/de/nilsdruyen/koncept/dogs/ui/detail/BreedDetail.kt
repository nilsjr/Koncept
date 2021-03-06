package de.nilsdruyen.koncept.dogs.ui.detail

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import de.nilsdruyen.koncept.dogs.entity.BreedImage
import de.nilsdruyen.koncept.dogs.ui.components.LoadingDoggo
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedDetail(viewModel: BreedDetailViewModel, navController: NavController) {
    val composeScope = rememberCoroutineScope()
    val state = viewModel.state.collectAsState()
    val scrollState = rememberTopAppBarScrollState()
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior(scrollState) }
    val color =
        TopAppBarDefaults.centerAlignedTopAppBarColors()
            .containerColor(scrollFraction = scrollBehavior.scrollFraction).value

    LaunchedEffect(Unit) {
        viewModel.intent.send(BreedDetailIntent.LoadImages)
    }

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .navigationBarsPadding(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Breed Detail") },
                scrollBehavior = scrollBehavior,
                actions = {
                    IconButton(onClick = {
                        composeScope.launch {
                            viewModel.intent.send(BreedDetailIntent.ToggleFavorite)
                        }
                    }) {
                        Icon(
                            imageVector = if (state.value.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Set Favorite"
                        )
                    }
                },
                modifier = Modifier
                    .background(color)
                    .statusBarsPadding()
            )
        },
        content = {
            Crossfade(targetState = state) { state ->
                BreedDetailContainer(
                    uiState = state.value,
                    onImageClick = { url ->
                        navController.navigate("image/$url")
                    },
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                )
            }
        }
    )
}

@Composable
fun BreedDetailContainer(uiState: BreedDetailState, onImageClick: (String) -> Unit, modifier: Modifier = Modifier) {
    when {
        uiState.isLoading -> {
            Box(modifier = modifier) {
                LoadingDoggo(
                    Modifier
                        .fillMaxSize(fraction = 0.5f)
                        .align(Alignment.Center)
                )
            }
        }
        uiState.images.isEmpty() -> {
            Box(modifier = modifier) {
                Text(
                    text = "No doggo images!",
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)
                )
            }
        }
        else -> BreedImageList(list = uiState.images, onImageClick, modifier)
    }
}

@Composable
fun BreedImageList(list: List<BreedImage>, onImageClick: (String) -> Unit, modifier: Modifier) {
    LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = modifier, content = {
        items(list) {
            BreedImage(it.url) {
                onImageClick(it.id)
            }
        }
    })
}

@Composable
fun BreedImage(url: String, onImageClick: () -> Unit) {
    val pxValue = with(LocalDensity.current) { 16.dp.toPx() }

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .transformations(RoundedCornersTransformation(pxValue))
            .build(),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(8.dp)
            .clickable { onImageClick() },
    )
}