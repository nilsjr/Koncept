@file:OptIn(ExperimentalAnimationApi::class, ExperimentalAnimationApi::class)

package de.nilsdruyen.koncept.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import de.nilsdruyen.koncept.base.navigation.NavigateTo
import de.nilsdruyen.koncept.dogs.ui.navigation.graph.dogDetailGraph
import de.nilsdruyen.koncept.dogs.ui.navigation.graph.dogTopLevelGraph
import de.nilsdruyen.koncept.dogs.ui.navigation.graph.favoriteTopLevelGraph
import de.nilsdruyen.koncept.dogs.ui.navigation.routes.BreedListRoute
import de.nilsdruyen.koncept.ui.WebScreen
import soup.compose.material.motion.animation.materialElevationScaleIn
import soup.compose.material.motion.animation.materialElevationScaleOut
import soup.compose.material.motion.animation.rememberSlideDistance
import soup.compose.material.motion.navigation.MaterialMotionNavHost
import soup.compose.material.motion.navigation.composable

@Composable
fun KonceptNavigation(
    navController: NavHostController,
    onNavigate: NavigateTo,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    startDestination: String = BreedListRoute.getGraphRoute(),
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
                    BreedListRoute.sortTypeResult,
                    it.ordinal
                )
                onBackClick()
            },
            slideDistance
        )
        favoriteTopLevelGraph(onNavigate) {
            dogDetailGraph(it, onNavigate, slideDistance)
        }
        webTopLevelGraph()
    }
}

fun NavGraphBuilder.webTopLevelGraph() {
    navigation(
        route = WebRoute.getGraphRoute(),
        startDestination = WebRoute.getStartDestination(),
    ) {
        composable(
            route = WebRoute.getStartDestination(),
            enterTransition = { materialElevationScaleIn() },
            exitTransition = { materialElevationScaleOut() },
        ) {
            WebScreen()
        }
    }
}