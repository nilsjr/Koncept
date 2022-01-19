package de.nilsdruyen.koncept.dogs.ui.list

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import de.nilsdruyen.koncept.common.ui.MaterialCard
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.dogs.ui.components.LoadingDoggo
import de.nilsdruyen.koncept.domain.Logger
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun DogList(viewModel: DogListViewModel, onBreedClick: (Int) -> Unit = {}) {
    val uiState = viewModel.state.collectAsState()
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.intent.send(DogListIntent.LoadIntent)
    }

    LaunchedEffect("events") {
        viewModel.effect.onEach {
            when (it) {
                is Effect.NavigateToDetail -> onBreedClick(it.breedId)
            }
        }.collect()
    }

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
                onClick = {}
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Localized description")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        content = {
            Crossfade(targetState = uiState) { state ->
                val currentState = state.value
                when {
                    currentState.isLoading -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            LoadingDoggo(
                                Modifier
                                    .fillMaxSize(fraction = 0.5f)
                                    .align(Alignment.Center)
                            )
                        }
                    }
                    currentState.list.isEmpty() -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = "No doggos!",
                                modifier = Modifier
                                    .padding(16.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    }
                    currentState.list.isNotEmpty() -> {
                        DogList(
                            listPosition = currentState.listPosition,
                            dogList = currentState.list,
                            showDog = { dog, listPosition ->
                                coroutineScope.launch {
                                    viewModel.intent.send(
                                        DogListIntent.ShowDetailAndSaveListPosition(
                                            dog.id,
                                            listPosition
                                        )
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun DogList(
    listPosition: ListPosition,
    dogList: List<Dog>,
    showDog: (Dog, ListPosition) -> Unit,
) {
    Logger.log("init state $listPosition")
    val listState = rememberLazyListState(listPosition.index, listPosition.offset)
    LazyColumn(
        contentPadding = PaddingValues(bottom = 4.dp),
        state = listState,
    ) {
        items(dogList) { dog ->
            DogItem(dog) {
                showDog(it, ListPosition(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset))
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