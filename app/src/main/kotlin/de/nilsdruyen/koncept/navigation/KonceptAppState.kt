package de.nilsdruyen.koncept.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import de.nilsdruyen.koncept.R
import de.nilsdruyen.koncept.base.navigation.TopLevelRoute
import de.nilsdruyen.koncept.design.system.Icon
import de.nilsdruyen.koncept.design.system.KonceptIcons
import de.nilsdruyen.koncept.dogs.ui.navigation.DogListRoute
import de.nilsdruyen.koncept.dogs.ui.navigation.FavoritesRoute
import de.nilsdruyen.koncept.domain.Logger.Companion.log

@Composable
fun rememberKonceptAppState(
//    navController: NavHostController = rememberNavController()
): KonceptAppState {
//    NavigationTrackingSideEffect(navController)
    return remember {
        KonceptAppState()
    }
}

@Stable
class KonceptAppState() {

//    val currentDestination: NavDestination?
//        @Composable get() = navController
//            .currentBackStackEntryAsState().value?.destination

    /**
     * Top level destinations to be used in the BottomBar and NavRail
     */
    val topLevelDestinations: List<TopLevelRoute> = listOf(
        TopLevelRoute(
            route = DogListRoute,
            selectedIcon = Icon.ImageVectorIcon(KonceptIcons.BreedList),
            unselectedIcon = Icon.ImageVectorIcon(KonceptIcons.BreedListFilled),
            iconTextId = R.string.breed_list_title
        ),
        TopLevelRoute(
            route = FavoritesRoute,
            selectedIcon = Icon.ImageVectorIcon(KonceptIcons.Favorites),
            unselectedIcon = Icon.ImageVectorIcon(KonceptIcons.FavoritesFilled),
            iconTextId = R.string.favorites_title
        ),
//        TopLevelRoute(
//            route = WebRoute.getGraphRoute(),
//            selectedIcon = Icon.ImageVectorIcon(KonceptIcons.Web),
//            unselectedIcon = Icon.ImageVectorIcon(KonceptIcons.WebFilled),
//            iconTextId = R.string.web_title
//        ),
    )

//    fun navigate(destination: KonceptNavDestination) {
//        when (destination) {
//            is KonceptNavDestination.TopLevelGraphDestination -> {
//                if (destination.route != navController.currentDestination?.route) {
//                    navController.navigate(destination.route) {
//                        popUpTo(navController.graph.findStartDestination().id) {
//                            saveState = true
//                        }
//                        // Avoid multiple copies of the same destination when
//                        // reselecting the same item
//                        launchSingleTop = true
//                        // Restore state when reselecting a previously selected item
//                        restoreState = true
//                    }
//                } else {
//                    // same top level destination do nothing
//                }
//            }
//            is KonceptNavDestination.GraphDestination -> navController.navigate(destination.route)
//            is KonceptNavDestination.NestedGraphDestination -> navController.navigate(destination.route)
//            is KonceptNavDestination.NestedNavDestination -> navController.navigate(destination.route)
//        }
//    }
//
//    fun onBackClick() {
//        navController.popBackStack()
//    }
}

@Composable
private fun NavigationTrackingSideEffect(navController: NavHostController) {
    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            log("${destination.route}")
        }

        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
}
