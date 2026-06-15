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
fun rememberKonceptAppState(): KonceptAppState = remember { KonceptAppState() }

@Stable
class KonceptAppState {

    /**
     * Top level destinations to be used in the BottomBar and NavRail
     */
    val topLevelDestinations: List<TopLevelRoute> = listOf(
        TopLevelRoute(
            route = DogListRoute,
            selectedIcon = Icon.ImageVectorIcon(KonceptIcons.BreedList),
            unselectedIcon = Icon.ImageVectorIcon(KonceptIcons.BreedListFilled),
            iconTextId = R.string.breed_list_title,
        ),
        TopLevelRoute(
            route = FavoritesRoute,
            selectedIcon = Icon.ImageVectorIcon(KonceptIcons.Favorites),
            unselectedIcon = Icon.ImageVectorIcon(KonceptIcons.FavoritesFilled),
            iconTextId = R.string.favorites_title,
        ),
    )
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
