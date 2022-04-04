package de.nilsdruyen.koncept.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import de.nilsdruyen.koncept.dogs.ui.detail.BreedDetail
import de.nilsdruyen.koncept.dogs.ui.detail.ImageDetail
import de.nilsdruyen.koncept.dogs.ui.list.DogListScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun KonceptApp() {
    val navController = rememberAnimatedNavController()
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()
    val surfaceColor = MaterialTheme.colorScheme.surface

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = surfaceColor,
            darkIcons = useDarkIcons
        )
    }

    ProvideWindowInsets {
        AnimatedNavHost(navController = navController, startDestination = "breedList") {
            composable(
                route = "breedList",
                enterTransition = { null },
                exitTransition = { null },
                popEnterTransition = { null },
                popExitTransition = { null },
            ) {
                DogListScreen(viewModel = hiltViewModel(), navController = navController)
            }
            composable("breedDetail/{breedId}", arguments = listOf(navArgument("breedId") {
                type = NavType.IntType
            })) {
                BreedDetail(hiltViewModel(), navController)
            }
            composable(
                route = "image/{id}",
                arguments = listOf(navArgument("id") { type = NavType.StringType }),
                enterTransition = { fadeIn() },
                exitTransition = { fadeOut() },
                popEnterTransition = { fadeIn() },
                popExitTransition = { fadeOut() },
            ) { backStackEntry ->
                ImageDetail(backStackEntry.arguments?.getString("id") ?: "")
            }
        }
    }
}