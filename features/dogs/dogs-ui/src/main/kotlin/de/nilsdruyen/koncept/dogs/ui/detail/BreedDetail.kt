package de.nilsdruyen.koncept.dogs.ui.detail

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import de.nilsdruyen.koncept.dogs.entity.BreedImage
import de.nilsdruyen.koncept.dogs.ui.components.LoadingDoggo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedDetail(viewModel: BreedDetailViewModel, navController: NavController) {
    val state = viewModel.state.collectAsState()
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }

    LaunchedEffect(Unit) {
        viewModel.intent.send(BreedDetailIntent.LoadImages)
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Breed Detail") },
                scrollBehavior = scrollBehavior
            )
        },
        content = {
            Crossfade(targetState = state) { state ->
                BreedDetailContainer(state.value) { url ->
                    navController.navigate("image/$url")
                }
            }
        }
    )
}

@Composable
fun BreedDetailContainer(uiState: BreedDetailState, onImageClick: (String) -> Unit) {
    when {
        uiState.isLoading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                LoadingDoggo(
                    Modifier
                        .fillMaxSize(fraction = 0.5f)
                        .align(Alignment.Center)
                )
            }
        }
        uiState.images.isEmpty() -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "No doggo images!",
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)
                )
            }
        }
        uiState.images.isNotEmpty() -> BreedImageList(list = uiState.images, onImageClick)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BreedImageList(list: List<BreedImage>, onImageClick: (String) -> Unit) {
    LazyVerticalGrid(cells = GridCells.Fixed(2), content = {
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

    Image(
        painter = rememberImagePainter(
            data = url,
            builder = {
                crossfade(true)
                transformations(RoundedCornersTransformation(pxValue))
            }
        ),
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize()
            .aspectRatio(1f)
            .padding(8.dp)
            .clickable { onImageClick() }
    )
}