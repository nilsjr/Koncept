package de.nilsdruyen.koncept.dogs.ui.list

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarScrollState
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import de.nilsdruyen.koncept.common.ui.KonceptTheme
import de.nilsdruyen.koncept.common.ui.MaterialCard
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.dogs.ui.components.LoadingDoggo
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogListScreen(viewModel: DogListViewModel, navController: NavController, modifier: Modifier = Modifier) {
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
        modifier = modifier,
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
    val appBarScrollState = rememberTopAppBarScrollState()
    val scrollBehavior = remember {
        TopAppBarDefaults.pinnedScrollBehavior(appBarScrollState)
    }
    val color =
        TopAppBarDefaults.smallTopAppBarColors().containerColor(scrollFraction = scrollBehavior.scrollFraction).value

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SmallTopAppBar(
                title = { Text("Doggo List") },
                modifier = Modifier
                    .background(color)
                    .statusBarsPadding(),
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
                    state.isLoading -> DogListLoading(Modifier.padding(it))
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
fun DogListLoading(modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        LoadingDoggo(
            Modifier
                .fillMaxSize(fraction = 0.5f)
                .align(Alignment.Center)
        )
    }
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

@Composable
fun DogItem(dog: Dog, showDog: (Dog) -> Unit) {
    MaterialCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { showDog(dog) },
        backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        ConstraintLayout(
            modifier = Modifier.padding(8.dp)
        ) {
            val (name) = createRefs()
            Text(
                text = dog.name,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .constrainAs(name) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    }
            )
        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun PreviewDogItem(dog: Dog = Dog(1, "Nils Hund")) {
    KonceptTheme {
        DogItem(dog = dog, showDog = {})
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