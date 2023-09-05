@file:OptIn(ExperimentalAnimationApi::class, ExperimentalAnimationApi::class)

package de.nilsdruyen.koncept.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import de.nilsdruyen.koncept.base.navigation.NavigateTo
import de.nilsdruyen.koncept.base.navigation.konceptComposable
import de.nilsdruyen.koncept.base.navigation.navigation
import de.nilsdruyen.koncept.dogs.ui.navigation.graph.breedDetailGraph
import de.nilsdruyen.koncept.dogs.ui.navigation.graph.breedTopLevelGraph
import de.nilsdruyen.koncept.dogs.ui.navigation.graph.favoriteTopLevelGraph
import de.nilsdruyen.koncept.dogs.ui.navigation.routes.BreedListRoute
import de.nilsdruyen.koncept.ui.DeeplinkSample
import de.nilsdruyen.koncept.ui.WebScreen
import soup.compose.material.motion.animation.materialElevationScaleIn
import soup.compose.material.motion.animation.materialElevationScaleOut
import soup.compose.material.motion.animation.rememberSlideDistance
import soup.compose.material.motion.navigation.MaterialMotionNavHost
import soup.compose.material.motion.navigation.composable

@Composable
fun KonceptNavHost(
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
        breedTopLevelGraph(
            onNavigate = onNavigate,
            setSortResult = {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    BreedListRoute.sortTypeResult,
                    it.ordinal
                )
                onBackClick()
            },
            slideDistance,
        )
        favoriteTopLevelGraph(onNavigate) {
            breedDetailGraph(it, onNavigate, slideDistance)
        }
        webTopLevelGraph()
        composable(
            route = "deeplink/{rawDate}",
            arguments = listOf(
                navArgument("rawDate") {
                    type = NavType.StringType
                },
                navArgument("rawDate2") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "koncept://deeplink/{rawDate}?rawDate2={rawDate2}"
                }
            )
        ) {
            DeeplinkSample()
        }
    }
}

fun NavGraphBuilder.webTopLevelGraph() = navigation(navRoute = WebRoute) {
    konceptComposable(
        navRoute = WebRoute,
        enterTransition = { materialElevationScaleIn() },
        exitTransition = { materialElevationScaleOut() },
    ) {
        WebScreen()
    }
}