package de.nilsdruyen.koncept.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import de.nilsdruyen.koncept.R
import de.nilsdruyen.koncept.base.navigation.TopLevelRoute
import de.nilsdruyen.koncept.design.system.Icon
import de.nilsdruyen.koncept.design.system.KonceptIcons
import de.nilsdruyen.koncept.dogs.ui.navigation.DogListRoute
import de.nilsdruyen.koncept.dogs.ui.navigation.FavoritesRoute

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
            testTag = "breed_list_graph",
        ),
        TopLevelRoute(
            route = FavoritesRoute,
            selectedIcon = Icon.ImageVectorIcon(KonceptIcons.Favorites),
            unselectedIcon = Icon.ImageVectorIcon(KonceptIcons.FavoritesFilled),
            iconTextId = R.string.favorites_title,
            testTag = "favorites_graph",
        ),
    )
}
