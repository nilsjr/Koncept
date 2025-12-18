@file:OptIn(ExperimentalSharedTransitionApi::class)

package de.nilsdruyen.koncept.ui

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import de.nilsdruyen.koncept.base.navigation.Navigator
import de.nilsdruyen.koncept.base.navigation.TopLevelRoute
import de.nilsdruyen.koncept.base.navigation.rememberNavigator
import de.nilsdruyen.koncept.design.system.Icon
import de.nilsdruyen.koncept.dogs.ui.favorites.Favorites
import de.nilsdruyen.koncept.dogs.ui.navigation.DogDetailRoute
import de.nilsdruyen.koncept.dogs.ui.navigation.DogListRoute
import de.nilsdruyen.koncept.dogs.ui.navigation.FavoritesRoute
import de.nilsdruyen.koncept.dogs.ui.navigation.dogRoutes
import de.nilsdruyen.koncept.domain.Logger.Companion.log
import de.nilsdruyen.koncept.navigation.WebRoute
import de.nilsdruyen.koncept.navigation.rememberKonceptAppState

@OptIn(
    ExperimentalComposeUiApi::class,
)
@Composable
fun KonceptApp() {
    val navigator = rememberNavigator(DogListRoute)
//    val bottomSheetNavigator = rememberBottomSheetNavigator()
//    val rootNavController = rememberNavController(bottomSheetNavigator)
//    val navController = rememberNavController(bottomSheetNavigator)

//    ModalBottomSheetLayout(
//        bottomSheetNavigator = bottomSheetNavigator,
//        modifier = Modifier
//            .fillMaxSize()
//            .semantics {
//                testTagsAsResourceId = true
//            }
//    ) {
    SharedTransitionLayout {
//            RootNavHost(
//                navController = rootNavController,
//                sharedTransitionScope = this,
//            ) {
//                MainBottomBarScreen(navController, it, this)
//            }
        MainBottomBarScreen(navigator, this)
    }
//    }
}

@Composable
fun MainBottomBarScreen(
    navigator: Navigator,
    sharedTransitionScope: SharedTransitionScope,
) {
    val state = rememberKonceptAppState()

    Scaffold(
        modifier = Modifier,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            KonceptBottomBar(
                destinations = state.topLevelDestinations,
                onNavigateToDestination = {
                    navigator.goTo(it)
                },
                currentDestination = navigator.backStack.last(),
            )
        },
    ) { padding ->
        NavDisplay(
            backStack = navigator.backStack,
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding),
            onBack = { navigator.goBack() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = entryProvider {
                dogRoutes(navigator, sharedTransitionScope)
                entry<FavoritesRoute> {
                    Favorites(
                        showBreed = {
                            navigator.goTo(DogDetailRoute(it))
                        }
                    )
                }
                entry<WebRoute> {
                    WebScreen()
                }
            }
        )
    }
}

@Composable
private fun KonceptBottomBar(
    destinations: List<TopLevelRoute>,
    onNavigateToDestination: (NavKey) -> Unit,
    currentDestination: NavKey?,
) {
    // TODO: navigation bar logic for current destination missing
    NavigationBar {
//        val routes = currentDestination?.hierarchy?.mapNotNull { it.route }?.toList() ?: emptyList()
//        log("nav stack $routes")
        destinations.forEach { item ->
//            val isSelected = routes.any { it == item.route }
            NavigationBarItem(
                selected = false,
                onClick = { onNavigateToDestination(item.route) },
                icon = {
                    when (val icon = if (false) item.selectedIcon else item.unselectedIcon) {
                        is Icon.ImageVectorIcon -> Icon(
                            imageVector = icon.imageVector,
                            contentDescription = null
                        )

                        is Icon.DrawableResourceIcon -> Icon(
                            painter = painterResource(id = icon.id),
                            contentDescription = null
                        )
                    }
                },
                alwaysShowLabel = false,
                label = {
                    Text(text = stringResource(id = item.iconTextId))
                },
                modifier = Modifier.testTag(item.route.toString())
            )
        }
    }
}
