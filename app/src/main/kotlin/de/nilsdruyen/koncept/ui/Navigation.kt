package de.nilsdruyen.koncept.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import de.nilsdruyen.koncept.base.navigation.KonceptNavDestination
import de.nilsdruyen.koncept.base.navigation.NavigateTo
import de.nilsdruyen.koncept.base.navigation.TopLevelRoute
import de.nilsdruyen.koncept.design.system.Icon
import de.nilsdruyen.koncept.domain.Logger.Companion.log
import de.nilsdruyen.koncept.navigation.KonceptNavHost
import de.nilsdruyen.koncept.navigation.RootNavHost
import de.nilsdruyen.koncept.navigation.rememberKonceptAppState

@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalMaterialNavigationApi::class,
)
@Composable
fun KonceptApp() {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val rootNavController = rememberNavController(bottomSheetNavigator)
    val navController = rememberNavController(bottomSheetNavigator)

    ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator,
        modifier = Modifier
            .fillMaxSize()
            .semantics {
                testTagsAsResourceId = true
            }
    ) {
        RootNavHost(navController = rootNavController) {
            MainBottomBarScreen(navController, it)
        }
    }
}

@Composable
fun MainBottomBarScreen(
    navController: NavHostController,
    navigateRoot: (String) -> Unit,
) {
    val state = rememberKonceptAppState(navController)
    Scaffold(
        modifier = Modifier,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            KonceptBottomBar(
                destinations = state.topLevelDestinations,
                onNavigateToDestination = state::navigate,
                currentDestination = state.currentDestination,
            )
        },
    ) { padding ->
        KonceptNavHost(
            navController = navController,
//            onBackClick = state::onBackClick,
            onNavigate = {
                if (it is KonceptNavDestination.NestedNavDestination) {
                    navigateRoot(it.route)
                } else {
                    state.navigate(it)
                }
            },
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding),
        )
    }
}

@Composable
private fun KonceptBottomBar(
    destinations: List<TopLevelRoute>,
    onNavigateToDestination: NavigateTo,
    currentDestination: NavDestination?,
) {
    NavigationBar {
        val routes = currentDestination?.hierarchy?.mapNotNull { it.route }?.toList() ?: emptyList()
        log("nav stack $routes")
        destinations.forEach { item ->
            val isSelected = routes.any { it == item.route }
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigateToDestination(item.navigate()) },
                icon = {
                    when (val icon = if (isSelected) item.selectedIcon else item.unselectedIcon) {
                        is Icon.ImageVectorIcon -> Icon(
                            imageVector = icon.imageVector,
                            contentDescription = null
                        )

                        is Icon.DrawableResourceIcon -> Icon(
                            painter = painterResource(id = icon.id),
                            contentDescription = null
                        )
                    }
                },
                alwaysShowLabel = false,
                label = {
                    Text(text = stringResource(id = item.iconTextId))
                },
                modifier = Modifier.testTag(item.route)
            )
        }
    }
}
