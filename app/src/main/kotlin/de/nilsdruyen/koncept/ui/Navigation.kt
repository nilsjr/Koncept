package de.nilsdruyen.koncept.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import de.nilsdruyen.koncept.dogs.list.DogList

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
        AnimatedNavHost(navController = navController, startDestination = "/") {
            composable(
                route = "/",
                enterTransition = { null },
                exitTransition = { null },
                popEnterTransition = { null },
                popExitTransition = { null },
            ) {
                DogList(
                    viewModel = hiltViewModel()
                )
            }
        }
    }
}