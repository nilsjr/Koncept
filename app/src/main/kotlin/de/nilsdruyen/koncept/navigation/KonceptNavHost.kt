package de.nilsdruyen.koncept.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import de.nilsdruyen.koncept.base.navigation.NavigationDestination
import de.nilsdruyen.koncept.dogs.ui.navigation.BreedListDestination
import de.nilsdruyen.koncept.dogs.ui.navigation.dogGraph
import de.nilsdruyen.koncept.ui.WebScreen
import soup.compose.material.motion.materialElevationScaleIn
import soup.compose.material.motion.materialElevationScaleOut
import soup.compose.material.motion.navigation.MaterialMotionNavHost
import soup.compose.material.motion.navigation.composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun KonceptNavigation(
    navController: NavHostController,
    onNavigate: (NavigationDestination) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    startDestination: String = BreedListDestination.route,
) {
    MaterialMotionNavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        dogGraph(
            onNavigate = onNavigate,
            setSortResult = {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    BreedListDestination.sortTypeResult,
                    it.ordinal
                )
                onBackClick()
            }
        )
        composable(
            route = WebDestination.route,
            enterMotionSpec = { materialElevationScaleIn() },
            exitMotionSpec = { materialElevationScaleOut() },
        ) {
            WebScreen()
        }
    }
}