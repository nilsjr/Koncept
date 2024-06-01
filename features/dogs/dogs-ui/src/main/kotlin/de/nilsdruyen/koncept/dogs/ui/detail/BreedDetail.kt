package de.nilsdruyen.koncept.dogs.ui.detail

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import de.nilsdruyen.koncept.common.ui.ImmutableList
import de.nilsdruyen.koncept.common.ui.dropBottomPadding
import de.nilsdruyen.koncept.common.ui.isEmpty
import de.nilsdruyen.koncept.dogs.entity.BreedImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedDetail(
    showImageDetail: (id: String) -> Unit,
    viewModel: BreedDetailViewModel = hiltViewModel(),
) {
    val composeScope = rememberCoroutineScope()
    val scrollState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(scrollState)
    val state = viewModel.state.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    )

    LaunchedEffect(Unit) {
        viewModel.intent.send(BreedDetailIntent.LoadImages)
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
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
                            imageVector = if (state.value.isFavorite) {
                                Icons.Default.Favorite
                            } else {
                                Icons.Default.FavoriteBorder
                            },
                            contentDescription = "Set Favorite"
                        )
                    }
                }
            )
        },
        content = {
            BreedDetailContainer(
                uiState = state.value,
                onImageClick = showImageDetail,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it.dropBottomPadding())
            )
        }
    )
}

@Composable
fun BreedDetailContainer(uiState: BreedDetailState, onImageClick: (String) -> Unit, modifier: Modifier = Modifier) {
    Crossfade(targetState = uiState) {
        when {
            it.isLoading -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = modifier,
                    content = {
                        items(5) {
                            ImagePlaceholder(
                                Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .padding(8.dp)
                            )
                        }
                    }
                )
            }

            it.images.isEmpty() -> {
                Box(modifier = modifier) {
                    Text(
                        text = "No doggo images!",
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.Center)
                    )
                }
            }

            else -> BreedImageList(list = it.images, onImageClick, modifier)
        }
    }
}

@Composable
fun BreedImageList(list: ImmutableList<BreedImage>, onImageClick: (String) -> Unit, modifier: Modifier = Modifier) {
    LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = modifier, content = {
        items(list.items) {
            BreedImage(
                url = it.url,
                onImageClick = {
                    onImageClick(it.id)
                }
            )
        }
    })
}

@Composable
fun BreedImage(url: String, onImageClick: () -> Unit, modifier: Modifier = Modifier) {
    val pxValue = with(LocalDensity.current) { 16.dp.toPx() }

    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .transformations(RoundedCornersTransformation(pxValue))
            .build(),
        contentDescription = null,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(8.dp)
    ) {
        val state = painter.state
        if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
            ImagePlaceholder(Modifier.fillMaxSize())
        } else {
            SubcomposeAsyncImageContent(
                modifier = Modifier.clickable { onImageClick() }
            )
        }
    }
}

@Composable
private fun ImagePlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier
            .clip(RoundedCornerShape(16.dp))
            .placeholder(
                visible = true,
                color = Color.LightGray,
                highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
            )
    )
}
