package de.nilsdruyen.koncept.dogs.ui.list

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.nilsdruyen.koncept.base.navigation.OnNavigate
import de.nilsdruyen.koncept.design.system.KonceptIcons
import de.nilsdruyen.koncept.design.system.KonceptTheme
import de.nilsdruyen.koncept.dogs.entity.BreedSortType
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.dogs.ui.components.Loading
import de.nilsdruyen.koncept.dogs.ui.navigation.BreedDetailsDestination
import de.nilsdruyen.koncept.dogs.ui.navigation.BreedListSortDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun DogListScreen(
    onNavigate: OnNavigate,
    sortTypeState: State<Int>,
    viewModel: DogListViewModel = hiltViewModel(),
) {
    val uiState = viewModel.state.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.events.onEach {
            when (it) {
                is DogListEvent.NavigateToDetail -> onNavigate(BreedDetailsDestination.buildRoute(it.breedId))
            }
        }.collect()
    }

    LaunchedEffect(sortTypeState.value) {
        viewModel.intent.send(DogListIntent.SortTypeChanged(BreedSortType.values()[sortTypeState.value]))
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
        showSortDialog = {
            onNavigate(BreedListSortDialog.createRoute(uiState.value.selectedType))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogListScreen(
    state: DogListState,
    modifier: Modifier = Modifier,
    startTask: () -> Unit = {},
    showDog: (Dog) -> Unit = {},
    showSortDialog: () -> Unit = {},
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
                title = { Text("Doggo List") },
                modifier = Modifier.testTag("appbar"),
                actions = {
                    IconButton(onClick = showSortDialog) {
                        BadgedBox(badge = { Badge { Text("1") } }) {
                            Icon(
                                imageVector = KonceptIcons.FilterList,
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
        content = { padding ->
            Crossfade(targetState = state, Modifier.padding(padding)) { state ->
                when {
                    state.isLoading -> Loading()
                    state.list.isEmpty() -> DogListEmpty()
                    else -> DogList(
                        scrollState = scrollState,
                        list = state.list,
                        showDog = { showDog(it) },
                    )
                }
            }
        }
    )
}

@Composable
fun DogListEmpty(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(
            text = "No doggos!",
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Center)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DogList(scrollState: LazyListState, list: List<Dog>, showDog: (Dog) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(
        state = scrollState,
        modifier = modifier
            .fillMaxSize()
            .testTag("dogList")
    ) {
        items(list, key = { it.id }) { dog ->
            DogItem(
                dog = dog,
                modifier = Modifier.animateItemPlacement(),
                showDog = showDog,
            )
        }
    }
}

@VisibleForTesting
@ExperimentalMaterial3Api
@Preview
@Composable
fun PreviewDogList(@PreviewParameter(DogListPreviewProvider::class) listState: DogListState) {
    KonceptTheme {
        DogListScreen(listState)
    }
}

class DogListPreviewProvider : PreviewParameterProvider<DogListState> {
    override val values: Sequence<DogListState> = sequenceOf(
        DogListState(),
        DogListState(
            List(15) {
                Dog(it, "Breed $it")
            }.toImmutable()
        ),
    )
}