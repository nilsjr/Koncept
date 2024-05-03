package de.nilsdruyen.koncept.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import de.nilsdruyen.koncept.base.navigation.NavigateTo
import de.nilsdruyen.koncept.base.navigation.konceptComposable
import de.nilsdruyen.koncept.base.navigation.navigation
import de.nilsdruyen.koncept.dogs.ui.detail.BreedDetail
import de.nilsdruyen.koncept.dogs.ui.navigation.graph.breedDetailGraph
import de.nilsdruyen.koncept.dogs.ui.navigation.graph.breedTopLevelGraph
import de.nilsdruyen.koncept.dogs.ui.navigation.graph.favoriteTopLevelGraph
import de.nilsdruyen.koncept.dogs.ui.navigation.routes.BreedDetailsRoute
import de.nilsdruyen.koncept.dogs.ui.navigation.routes.BreedListRoute
import de.nilsdruyen.koncept.ui.DeeplinkSample
import de.nilsdruyen.koncept.ui.WebScreen

@Composable
fun RootNavHost(
    navController: NavHostController,
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
            BreedDetail(
                showImageDetail = {
//                onNavigate(ImageDetailRoute.createRoute(base, it))
                }
            )
        }
        composable("login") {
        }
    }
}

@Composable
fun KonceptNavHost(
    navController: NavHostController,
    onNavigate: NavigateTo,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    startDestination: String = BreedListRoute.getGraphRoute(),
) {
    NavHost(
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
        )
        favoriteTopLevelGraph(onNavigate) {
            breedDetailGraph(it, onNavigate)
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
    ) {
        WebScreen()
    }
}
