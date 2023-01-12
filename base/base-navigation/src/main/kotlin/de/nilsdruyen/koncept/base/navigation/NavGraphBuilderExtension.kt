package de.nilsdruyen.koncept.base.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import soup.compose.material.motion.navigation.composable

const val FULLSCREEN_NAVIGATION_KEY = "FULLSCREEN"

fun NavGraphBuilder.navigation(
    navRoute: KonceptNavRoute.GraphNavRoute,
    builder: NavGraphBuilder.() -> Unit,
) {
    navigation(
        route = navRoute.getGraphRoute(),
        startDestination = navRoute.getStartDestination(),
        builder = builder,
    )
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.konceptComposable(
    navRoute: KonceptNavRoute.GraphNavRoute,
    enterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?)? = null,
    exitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = null,
    popEnterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?)? = enterTransition,
    popExitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = exitTransition,
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit,
) {
    composable(
        route = navRoute.getStartDestination(),
        arguments = navRoute.arguments() + listOf(
            navArgument(FULLSCREEN_NAVIGATION_KEY) {
                type = NavType.BoolType
                defaultValue = true
            }
        ),
        deepLinks = navRoute.deepLinks(),
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
        content = content,
    )
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.konceptComposable(
    navRoute: KonceptNavRoute.NestedNavRoute,
    graphRoute: KonceptNavRoute.GraphNavRoute? = null,
    enterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?)? = null,
    exitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = null,
    popEnterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?)? = enterTransition,
    popExitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = exitTransition,
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit,
) {
    composable(
        route = navRoute.getNestedRoute(graphRoute),
        arguments = navRoute.arguments() + listOf(
            navArgument(FULLSCREEN_NAVIGATION_KEY) {
                type = NavType.BoolType
                defaultValue = true
            }
        ),
        deepLinks = navRoute.deepLinks(),
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
        content = content,
    )
}

@ExperimentalMaterialNavigationApi
fun NavGraphBuilder.bottomSheet(
    navRoute: KonceptNavRoute.NestedNavRoute,
    graphRoute: KonceptNavRoute.GraphNavRoute? = null,
    content: @Composable ColumnScope.(backstackEntry: NavBackStackEntry) -> Unit
) {
    bottomSheet(
        route = navRoute.getNestedRoute(graphRoute),
        arguments = navRoute.arguments(),
        deepLinks = navRoute.deepLinks(),
        content = content,
    )
}