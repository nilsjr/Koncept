package de.nilsdruyen.koncept.dogs.ui.list

import androidx.annotation.VisibleForTesting
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.nilsdruyen.koncept.common.ui.KonceptTheme
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.dogs.ui.components.Loading
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogListScreen(viewModel: DogListViewModel, navController: NavController) {
    val uiState = viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.events.onEach {
            when (it) {
                is DogListEvent.NavigateToDetail -> navController.navigate("breedDetail/${it.breedId}")
            }
        }.collect()
    }

    DogListScreen(
        state = uiState.value,
        startTask = {
            coroutineScope.launch {
                viewModel.intent.send(DogListIntent.StartLongTask)
            }
        },
        showDog = { dog ->
            coroutineScope.launch {
                viewModel.intent.send(DogListIntent.ShowDetailAndSaveListPosition(dog.id))
            }
        },
    )
}

@ExperimentalMaterial3Api
@Composable
fun DogListScreen(
    state: DogListState,
    modifier: Modifier = Modifier,
    startTask: () -> Unit = {},
    showDog: (Dog) -> Unit = {},
) {
    val scrollState = rememberLazyListState()
    val appBarScrollState = rememberTopAppBarState()
    val scrollBehavior = remember {
        TopAppBarDefaults.pinnedScrollBehavior(appBarScrollState)
    }
    val color =
        TopAppBarDefaults.smallTopAppBarColors().containerColor(appBarScrollState.contentOffset)

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SmallTopAppBar(
                title = { Text("Doggo List") },
                modifier = Modifier
                    .background(color.value)
                    .statusBarsPadding()
                    .testTag("appbar"),
                actions = {
                    IconButton(onClick = {}) {
                        BadgedBox(badge = { Badge { Text("1") } }) {
                            Icon(
                                imageVector = Icons.Filled.FilterList,
                                contentDescription = "Filter Games"
                            )
                        }
                    }
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search Game"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    startTask()
                }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Localized description")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        content = {
            Crossfade(targetState = state) { state ->
                when {
                    state.isLoading -> Loading(Modifier.padding(it))
                    state.list.isEmpty() -> DogListEmpty(Modifier.padding(it))
                    else -> DogList(
                        scrollState = scrollState,
                        list = state.list,
                        showDog = { showDog(it) },
                        modifier = Modifier.padding(it),
                    )
                }
            }
        }
    )
}

@Composable
fun DogListEmpty(modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(
            text = "No doggos!",
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun DogList(scrollState: LazyListState, list: List<Dog>, showDog: (Dog) -> Unit, modifier: Modifier) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 4.dp),
        state = scrollState,
        modifier = modifier
            .fillMaxSize()
            .testTag("dogList")
    ) {
        items(list) { dog ->
            DogItem(dog) {
                showDog(it)
            }
        }
    }
}

@VisibleForTesting
@ExperimentalMaterial3Api
@Preview
@Composable
fun PreviewDogList(@PreviewParameter(DogListPreviewProvider::class) listState: DogListState) {
    val state = derivedStateOf { listState }
    KonceptTheme {
        DogListScreen(state.value)
    }
}

class DogListPreviewProvider : PreviewParameterProvider<DogListState> {
    override val values: Sequence<DogListState> = sequenceOf(
        DogListState(),
        DogListState(List(15) {
            Dog(it, "Breed $it")
        }),
    )
}