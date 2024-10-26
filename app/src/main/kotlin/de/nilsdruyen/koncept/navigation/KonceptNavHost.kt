@file:OptIn(ExperimentalSharedTransitionApi::class)

package de.nilsdruyen.koncept.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import de.nilsdruyen.koncept.base.navigation.NavigateTo
import de.nilsdruyen.koncept.dogs.ui.detail.BreedDetailScreen
import de.nilsdruyen.koncept.dogs.ui.image.ImageDetailScreen
import de.nilsdruyen.koncept.dogs.ui.image.ImageRoute
import de.nilsdruyen.koncept.dogs.ui.list.DogGraph
import de.nilsdruyen.koncept.dogs.ui.navigation.graph.breedTopLevelGraph
import de.nilsdruyen.koncept.dogs.ui.navigation.graph.favoriteTopLevelGraph
import de.nilsdruyen.koncept.dogs.ui.navigation.routes.BreedDetailsRoute
import de.nilsdruyen.koncept.dogs.ui.navigation.routes.BreedListRoute
import de.nilsdruyen.koncept.ui.DeeplinkSample
import de.nilsdruyen.koncept.ui.WebScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RootNavHost(
    navController: NavHostController,
    sharedTransitionScope: SharedTransitionScope,
    content: @Composable (navigateRoot: (String) -> Unit) -> Unit,
) {
    val navigateRoot = { route: String ->
        navController.navigate(route)
    }
    NavHost(
        navController = navController,
        startDestination = "root",
        modifier = Modifier,
    ) {
        composable("root") {
            content(navigateRoot)
        }
        composable(
            "breed_detail/{breedId}",
            arguments = BreedDetailsRoute.pathParameters(),
        ) {
            BreedDetailScreen(
                showImageDetail = { navController.navigate(ImageRoute(it)) },
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = this,
            )
        }
        composable<ImageRoute> {
            val imageRoute: ImageRoute = it.toRoute()
            with(sharedTransitionScope) {
                ImageDetailScreen(
                    id = imageRoute.id,
                    imageModifier = Modifier.sharedElement(
                        sharedTransitionScope.rememberSharedContentState(key = "image-$id"),
                        animatedVisibilityScope = this@composable
                    )
                )
            }
        }
//        composable(
//            route = "image?imageId={imageId}",
//            arguments = listOf(
//                navArgument(ImageDetailRoute.imageIdArg) {
//                    type = NavType.StringType
//                }
//            ),
//        ) { backStackEntry ->
//            with(sharedTransitionScope) {
//                ImageDetailScreen(
//                    id = ImageDetailRoute.fromBackStackEntry(backStackEntry).imageId,
//                    imageModifier = Modifier.sharedElement(
//                        sharedTransitionScope.rememberSharedContentState(key = "image-$id"),
//                        animatedVisibilityScope = animatedContentScope
//                    )
//                )
//            }
//        }
    }
}

@Composable
fun KonceptNavHost(
    navController: NavHostController,
    onNavigate: NavigateTo,
//    onBackClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    modifier: Modifier = Modifier,
    startDestination: String = BreedListRoute.getGraphRoute(),
) {
    NavHost(
        navController = navController,
        startDestination = DogGraph,
        modifier = modifier,
    ) {
        breedTopLevelGraph(
            onNavigate = onNavigate,
//            setSortResult = {
//                navController.previousBackStackEntry?.savedStateHandle?.set(
//                    BreedListRoute.sortTypeResult,
//                    it.ordinal
//                )
//                onBackClick()
//            },
        )
        favoriteTopLevelGraph(onNavigate) {

        }
        composable<WebRoute> {
            WebScreen()
        }
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
