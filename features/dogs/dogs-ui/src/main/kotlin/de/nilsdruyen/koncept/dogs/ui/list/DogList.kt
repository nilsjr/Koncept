package de.nilsdruyen.koncept.dogs.ui.list

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.nilsdruyen.koncept.common.ui.dropBottomPadding
import de.nilsdruyen.koncept.common.ui.isEmpty
import de.nilsdruyen.koncept.common.ui.toImmutable
import de.nilsdruyen.koncept.design.system.KonceptIcons
import de.nilsdruyen.koncept.design.system.KonceptTheme
import de.nilsdruyen.koncept.dogs.entity.BreedId
import de.nilsdruyen.koncept.dogs.entity.BreedSortType
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.dogs.ui.components.Loading
import de.nilsdruyen.koncept.domain.sendIn
import kotlinx.coroutines.launch

@Composable
fun DogListScreen(
    sortTypeState: State<Int>,
    showDetail: (BreedId) -> Unit,
    showSortDialog: (BreedSortType) -> Unit,
    viewModel: DogListViewModel = hiltViewModel(),
) {
    val uiState = viewModel.state.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiState.value.navigateTo) {
        val id = uiState.value.navigateTo
        if (id != null) {
            viewModel.sendIntent(DogListIntent.NavigationConsumed)
            showDetail(id)
        }
    }

    LaunchedEffect(sortTypeState.value) {
        viewModel.intent.send(DogListIntent.SortTypeChanged(BreedSortType.values()[sortTypeState.value]))
    }

    DogListScreen(
        state = uiState.value,
        showDog = { dog ->
            coroutineScope.launch {
                viewModel.intent.send(DogListIntent.ShowDetailAndSaveListPosition(dog.id))
            }
        },
        showSortDialog = { showSortDialog(uiState.value.selectedType) },
        reloadList = {
            viewModel.intent.sendIn(coroutineScope, DogListIntent.Reload)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogListScreen(
    state: DogListState,
    showDog: (Dog) -> Unit = {},
    showSortDialog: () -> Unit = {},
    reloadList: () -> Unit = {},
) {
    val appBarScrollState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(appBarScrollState)

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
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
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = {
//                    startTask()
//                }
//            ) {
//                Icon(Icons.Filled.Add, contentDescription = "Localized description")
//            }
//        },
//        floatingActionButtonPosition = FabPosition.End,
        content = { padding ->
            val containerModifier = Modifier.padding(padding.dropBottomPadding())
            Crossfade(targetState = state) { state ->
                when {
                    state.isLoading && state.list.isEmpty() -> Loading(containerModifier)
                    state.list.isEmpty() -> DogListEmpty(containerModifier)
                    else -> DogList(
                        state = state,
                        showDog = { showDog(it) },
                        reloadList = reloadList,
                        modifier = containerModifier,
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
fun DogList(
    state: DogListState,
    showDog: (Dog) -> Unit,
    reloadList: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberLazyListState()
    val pullRefreshState = rememberPullRefreshState(refreshing = state.isLoading, onRefresh = reloadList)

    Box(modifier.pullRefresh(pullRefreshState)) {
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .testTag("dogList")
        ) {
            items(state.list.items, key = { it.id.value }) { dog ->
                DogItem(
                    dog = dog,
                    modifier = Modifier.animateItemPlacement(),
                    showDog = showDog,
                )
            }
        }
//        LazyVerticalGrid(columns = GridCells.Fixed(2), state = scrollState) {
//            list.items.forEach {
//                item(span = { GridItemSpan(2) }) {
//                    LazyRow(Modifier.fillMaxWidth()) {
//                        items(it.breed, key = { it.id }) {
//                            DogGridItem(it)
//                        }
//                    }
//                }
//            }
//            items(list.items, key = { it.name }) {
//                LazyRow(Modifier.fillMaxWidth()) {
//                    items(it.breed, key = { it.id }) {
//                        DogGridItem(it)
//                    }
//                }
//            }
//        }
        PullRefreshIndicator(
            refreshing = state.isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
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
            list = List(6) {
                Dog(BreedId(it), "Breed $it")
            }.toImmutable()
        )
//        DogListState(
//            List(4) {
//                DogGroup(
//                    name = "A$it",
//                    breed = List(6) {
//                        Dog(it, "Breed $it")
//                    }
//                )
//            }.toImmutable()
//        )
    )
}