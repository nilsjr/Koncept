package de.nilsdruyen.koncept.dogs.ui.list

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun DogListScreen(viewModel: DogListViewModel, navController: NavController) {
    val uiState = viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.intent.send(DogListIntent.LoadIntent)
        viewModel.effect.onEach {
            when (it) {
                is Effect.NavigateToDetail -> navController.navigate("breedDetail/${it.breedId}")
            }
        }.collect()
    }

    DogListScreen(uiState, {
        coroutineScope.launch {
            viewModel.intent.send(DogListIntent.StartLongTask)
        }
    }) { dog ->
        coroutineScope.launch {
            viewModel.intent.send(DogListIntent.ShowDetailAndSaveListPosition(dog.id))
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun DogListScreen(
    state: State<DogListState>,
    startTask: () -> Unit,
    showDog: (Dog) -> Unit,
) {
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
    val scrollState = rememberLazyListState()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SmallTopAppBar(
                title = { Text("Doggo List") },
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
                val currentState = state.value
                when {
                    currentState.isLoading -> DogListLoading()
                    currentState.list.isEmpty() -> DogListEmpty()
                    currentState.list.isNotEmpty() -> DogList(scrollState, currentState.list) {
                        showDog(it)
                    }
                }
            }
        }
    )
}

@Composable
fun DogListLoading() {
    Box(modifier = Modifier.fillMaxSize()) {
        LoadingDoggo(
            Modifier
                .fillMaxSize(fraction = 0.5f)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun DogListEmpty() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "No doggos!",
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun DogList(scrollState: LazyListState, list: List<Dog>, showDog: (Dog) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 4.dp),
        state = scrollState,
        modifier = Modifier
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
        DogListScreen(state, {}) {}
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