@file:OptIn(ExperimentalAnimationApi::class)

package de.nilsdruyen.koncept.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import de.nilsdruyen.koncept.base.navigation.NavigationDestination
import de.nilsdruyen.koncept.dogs.ui.navigation.BreedListDestination
import de.nilsdruyen.koncept.dogs.ui.navigation.BreedTopLevel
import de.nilsdruyen.koncept.dogs.ui.navigation.dogTopLevelGraph
import de.nilsdruyen.koncept.dogs.ui.navigation.favoriteTopLevelGraph
import de.nilsdruyen.koncept.ui.WebScreen
import soup.compose.material.motion.animation.materialElevationScaleIn
import soup.compose.material.motion.animation.materialElevationScaleOut
import soup.compose.material.motion.animation.rememberSlideDistance
import soup.compose.material.motion.navigation.MaterialMotionNavHost
import soup.compose.material.motion.navigation.composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun KonceptNavigation(
    navController: NavHostController,
    onNavigate: (NavigationDestination) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    startDestination: String = BreedTopLevel.root,
) {
    val slideDistance = rememberSlideDistance()
    MaterialMotionNavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        dogTopLevelGraph(
            onNavigate = onNavigate,
            setSortResult = {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    BreedListDestination.sortTypeResult,
                    it.ordinal
                )
                onBackClick()
            },
            slideDistance
        )
        favoriteTopLevelGraph(onNavigate = onNavigate, slideDistance)
        webTopLevelGraph()
    }
}

fun NavGraphBuilder.webTopLevelGraph() {
    navigation(
        route = WebTopLevel.root,
        startDestination = WebDestination.createRoute(WebTopLevel.root),
    ) {
        composable(
            route = WebDestination.createRoute(WebTopLevel.root),
            enterTransition = { materialElevationScaleIn() },
            exitTransition = { materialElevationScaleOut() },
        ) {
            WebScreen()
        }
    }
}