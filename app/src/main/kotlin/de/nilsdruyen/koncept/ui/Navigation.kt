package de.nilsdruyen.koncept.ui

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import de.nilsdruyen.koncept.dogs.ui.detail.BreedDetail
import de.nilsdruyen.koncept.dogs.ui.detail.image.ImageDetail
import de.nilsdruyen.koncept.dogs.ui.favorites.Favorites
import de.nilsdruyen.koncept.dogs.ui.list.DogListScreen
import soup.compose.material.motion.materialElevationScaleIn
import soup.compose.material.motion.materialElevationScaleOut
import soup.compose.material.motion.materialFadeThroughIn
import soup.compose.material.motion.materialFadeThroughOut
import soup.compose.material.motion.materialSharedAxisXIn
import soup.compose.material.motion.materialSharedAxisXOut
import soup.compose.material.motion.navigation.MaterialMotionNavHost
import soup.compose.material.motion.navigation.composable
import soup.compose.material.motion.navigation.rememberMaterialMotionNavController

val showBottomBarFor = listOf(
    "breedList",
    "favorites"
)

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun KonceptApp() {
    val navController = rememberMaterialMotionNavController()
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons
        )
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBottomBar = derivedStateOf {
        val dest = currentDestination ?: NavDestination("breedList")
        dest.route in showBottomBarFor
    }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar.value,
                enter = expandVertically(),
                exit = shrinkVertically(),
            ) {
                BottomNavigation(
                    modifier = Modifier.navigationBarsPadding(),
                    backgroundColor = MaterialTheme.colorScheme.surface,
                ) {
                    BottomBarItem.values().forEach { item ->
                        BottomNavigationItem(
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(imageVector = item.icon, contentDescription = null)
                            },
                            label = {
                                Text(text = stringResource(id = item.labelRes))
                            }
                        )
                    }
                }
            }
        },
        modifier = Modifier,
    ) {
        KonceptNavigation(navController = navController, Modifier.padding(it))
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun KonceptNavigation(navController: NavHostController, modifier: Modifier) {
    MaterialMotionNavHost(navController = navController, startDestination = "breedList", modifier = modifier) {
        composable(
            route = "breedList",
            enterMotionSpec = { materialFadeThroughIn() },
            exitMotionSpec = { materialFadeThroughOut() },
        ) {
            DogListScreen(viewModel = hiltViewModel(), navController = navController)
        }
        composable(
            route = "favorites",
            enterMotionSpec = { materialFadeThroughIn() },
            exitMotionSpec = { materialFadeThroughOut() },
        ) {
            Favorites(viewModel = hiltViewModel())
        }
        composable(
            route = "breedDetail/{breedId}",
            enterMotionSpec = { materialSharedAxisXIn() },
            exitMotionSpec = { materialSharedAxisXOut() },
            arguments = listOf(navArgument("breedId") {
                type = NavType.IntType
            })
        ) {
            BreedDetail(hiltViewModel(), navController)
        }
        composable(
            route = "image/{id}",
            enterMotionSpec = { materialElevationScaleIn() },
            exitMotionSpec = { materialElevationScaleOut() },
            arguments = listOf(navArgument("id") { type = NavType.StringType }),
        ) { backStackEntry ->
            ImageDetail(backStackEntry.arguments?.getString("id") ?: "")
        }
    }
}

enum class BottomBarItem(
    val route: String,
    val icon: ImageVector,
    @StringRes val labelRes: Int,
) {
    BreedList("breedList", Icons.Default.List, de.nilsdruyen.koncept.dogs.ui.R.string.dog_list_all),
    BreedFavorites("favorites", Icons.Default.Favorite, de.nilsdruyen.koncept.dogs.ui.R.string.dog_list_favorites)
}