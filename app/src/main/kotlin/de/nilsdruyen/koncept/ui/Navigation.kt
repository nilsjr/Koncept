package de.nilsdruyen.koncept.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import de.nilsdruyen.koncept.dogs.ui.detail.BreedDetail
import de.nilsdruyen.koncept.dogs.ui.detail.ImageDetail
import de.nilsdruyen.koncept.dogs.ui.list.DogListScreen
import soup.compose.material.motion.holdIn
import soup.compose.material.motion.holdOut
import soup.compose.material.motion.materialSharedAxisXIn
import soup.compose.material.motion.materialSharedAxisXOut
import soup.compose.material.motion.navigation.MaterialMotionNavHost
import soup.compose.material.motion.navigation.composable
import soup.compose.material.motion.navigation.rememberMaterialMotionNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun KonceptApp() {
    val navController = rememberAnimatedNavController()
    val navController2 = rememberMaterialMotionNavController()

    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons
        )
    }

    ProvideWindowInsets {
        MaterialMotionNavHost(navController = navController2, startDestination = "breedList") {
            composable(
                route = "breedList",
//                enterTransition = { null },
//                exitTransition = { null },
//                popEnterTransition = { null },
//                popExitTransition = { null },
                enterMotionSpec = { holdIn() },
                exitMotionSpec = { holdOut() },
            ) {
                DogListScreen(viewModel = hiltViewModel(), navController = navController2)
            }
            composable(
                route = "breedDetail/{breedId}",
                enterMotionSpec = { materialSharedAxisXIn() },
                exitMotionSpec = { materialSharedAxisXOut() },
                arguments = listOf(navArgument("breedId") {
                    type = NavType.IntType
                })
            ) {
                BreedDetail(hiltViewModel(), navController2)
            }
            composable(
                route = "image/{id}",
                arguments = listOf(navArgument("id") { type = NavType.StringType }),
//                enterTransition = { fadeIn() },
//                exitTransition = { fadeOut() },
//                popEnterTransition = { fadeIn() },
//                popExitTransition = { fadeOut() },
            ) { backStackEntry ->
                ImageDetail(backStackEntry.arguments?.getString("id") ?: "")
            }
        }
    }
}