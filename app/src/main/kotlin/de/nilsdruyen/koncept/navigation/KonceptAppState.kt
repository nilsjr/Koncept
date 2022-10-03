package de.nilsdruyen.koncept.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import de.nilsdruyen.koncept.R
import de.nilsdruyen.koncept.base.navigation.KonceptNavDestination
import de.nilsdruyen.koncept.base.navigation.TopLevelDestination
import de.nilsdruyen.koncept.design.system.Icon
import de.nilsdruyen.koncept.design.system.KonceptIcons
import de.nilsdruyen.koncept.dogs.ui.navigation.BreedListDestination
import de.nilsdruyen.koncept.dogs.ui.navigation.FavoritesDestination

@Composable
fun rememberKonceptAppState(
    navController: NavHostController = rememberNavController()
): KonceptAppState {
    NavigationTrackingSideEffect(navController)
    return remember(navController) {
        KonceptAppState(navController)
    }
}

@Stable
class KonceptAppState(
    val navController: NavHostController,
) {

    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    /**
     * Top level destinations to be used in the BottomBar and NavRail
     */
    val topLevelDestinations: List<TopLevelDestination> = listOf(
        TopLevelDestination(
            route = BreedListDestination.route,
            destination = BreedListDestination.destination,
            selectedIcon = Icon.ImageVectorIcon(KonceptIcons.BreedList),
            unselectedIcon = Icon.ImageVectorIcon(KonceptIcons.BreedListFilled),
            iconTextId = R.string.breed_list_title
        ),
        TopLevelDestination(
            route = FavoritesDestination.route,
            destination = FavoritesDestination.destination,
            selectedIcon = Icon.ImageVectorIcon(KonceptIcons.Favorites),
            unselectedIcon = Icon.ImageVectorIcon(KonceptIcons.FavoritesFilled),
            iconTextId = R.string.favorites_title
        ),
        TopLevelDestination(
            route = WebDestination.route,
            destination = WebDestination.destination,
            selectedIcon = Icon.ImageVectorIcon(KonceptIcons.Web),
            unselectedIcon = Icon.ImageVectorIcon(KonceptIcons.WebFilled),
            iconTextId = R.string.web_title
        ),
    )

    fun navigate(destination: KonceptNavDestination, route: String? = null) {
        if (destination is TopLevelDestination) {
            if (destination.route != navController.currentDestination?.route) {
                navController.navigate(route ?: destination.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    // Avoid multiple copies of the same destination when
                    // reselecting the same item
                    launchSingleTop = true
                    // Restore state when reselecting a previously selected item
                    restoreState = true
                }
            } else {
                // same top level destination do nothing
            }
        } else {
            navController.navigate(route ?: destination.route)
        }
    }

    fun onBackClick() {
        navController.popBackStack()
    }
}

@Composable
private fun NavigationTrackingSideEffect(navController: NavHostController) {
    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            // TODO: implement logging, tracking whatever
        }

        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
}