package de.nilsdruyen.koncept.dogs.ui.list

import androidx.activity.compose.BackHandler
import androidx.annotation.VisibleForTesting
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.nilsdruyen.koncept.common.ui.isEmpty
import de.nilsdruyen.koncept.common.ui.toImmutable
import de.nilsdruyen.koncept.design.system.KonceptIcons
import de.nilsdruyen.koncept.design.system.KonceptTheme
import de.nilsdruyen.koncept.dogs.entity.BreedId
import de.nilsdruyen.koncept.dogs.entity.BreedSortType
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.dogs.ui.components.Loading

@Composable
fun DogListScreen(
    sortTypeState: State<Int>,
    showDetail: (BreedId) -> Unit,
    showSortDialog: (BreedSortType) -> Unit,
    viewModel: DogListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.navigateTo) {
        val id = state.navigateTo
        if (id != null) {
            viewModel.sendIntent(DogListIntent.NavigationConsumed)
            showDetail(id)
        }
    }

    LaunchedEffect(sortTypeState.value) {
        viewModel.sendIntent(DogListIntent.SortTypeChanged(BreedSortType.entries[sortTypeState.value]))
    }

    DogListScreen(
        state = state,
        showDog = { dog ->
            viewModel.sendIntent(DogListIntent.ShowDetailAndSaveListPosition(dog.id))
        },
        showSortDialog = { showSortDialog(state.selectedType) },
        reloadList = {
            viewModel.sendIntent(DogListIntent.Reload)
        },
        inputChange = {
            viewModel.sendIntent(DogListIntent.InputChange(it))
        },
        searchForQuery = {
            viewModel.sendIntent(DogListIntent.Search)
        },
    ) {
        BackHandler {
            viewModel.sendIntent(DogListIntent.BackFromSearch)
        }

        if (!state.searchResult.isNullOrEmpty()) {
            SearchResult(
                searchResult = state.searchResult.orEmpty(),
                onClick = {},
                modifier = Modifier.systemBarsPadding(),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogListScreen(
    state: DogListState,
    showDog: (Dog) -> Unit = {},
    showSortDialog: () -> Unit = {},
    reloadList: () -> Unit = {},
    inputChange: (String) -> Unit = {},
    searchForQuery: () -> Unit = {},
    searchResult: @Composable () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val pullRefreshState = rememberPullToRefreshState { !state.activeSearch }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(pullRefreshState.isRefreshing) {
        if (pullRefreshState.isRefreshing) reloadList()
    }

    val searchModifier = if (state.activeSearch) {
        Modifier.systemBarsPadding()
    } else {
        Modifier
    }

    Scaffold(
        modifier = Modifier
            .nestedScroll(pullRefreshState.nestedScrollConnection)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AnimatedVisibility(visible = !state.activeSearch) {
                TopAppBar(
                    title = { Text("Doggo List") },
                    modifier = Modifier.testTag("appbar"),
                    actions = {
                        IconButton(onClick = showSortDialog) {
                            Icon(
                                imageVector = KonceptIcons.FilterList,
                                contentDescription = "Filter Games"
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                when {
                    state.isLoading && state.list.isEmpty() -> Loading()
                    state.list.isEmpty() -> {
                        Text(
                            text = "No doggos!",
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.Center)
                        )
                    }

                    else -> {
                        DogList(
                            state = state,
                            showDog = showDog,
                        )
                        SearchBar(
                            query = state.input,
                            active = state.activeSearch,
                            onQueryChange = { query ->
                                inputChange(query)
                            },
                            onSearch = {
                                searchForQuery()
                                focusManager.clearFocus()
                            },
                            onActiveChange = {},
                            placeholder = {
                                Text("Search breed")
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "Search breeds"
                                )
                            },
                            windowInsets = WindowInsets(0, 0, 0, 0),
                            modifier = searchModifier.align(Alignment.TopCenter),
                        ) {
                            searchResult()
                        }
                        PullToRefreshContainer(
                            modifier = Modifier.align(Alignment.TopCenter),
                            state = pullRefreshState,
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DogList(
    state: DogListState,
    showDog: (Dog) -> Unit,
) {
    LazyColumn(
        state = rememberLazyListState(),
        modifier = Modifier
            .fillMaxSize()
            .testTag("dogList"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        item { Spacer(modifier = Modifier.height(64.dp)) }
        items(state.list.items, key = { it.id.value }) { dog ->
            DogItem(
                dog = dog,
                modifier = Modifier.animateItemPlacement(),
                showDog = showDog,
            )
        }
        item { Spacer(modifier = Modifier.height(0.dp)) }
    }
}

@Composable
fun SearchResult(
    searchResult: List<Dog>,
    onClick: (Dog) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        state = rememberLazyListState(),
        modifier = modifier,
    ) {
        items(searchResult) {
            DogListItem(
                dog = it,
                onClick = {
                    onClick(it)
                }
            )
        }
    }
}

@VisibleForTesting
@ExperimentalMaterial3Api
@Preview
@Composable
private fun PreviewDogList(@PreviewParameter(DogListPreviewProvider::class) listState: DogListState) {
    KonceptTheme {
        DogListScreen(listState) {}
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
    )
}
