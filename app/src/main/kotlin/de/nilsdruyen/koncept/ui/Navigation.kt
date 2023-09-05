package de.nilsdruyen.koncept.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import de.nilsdruyen.koncept.base.navigation.NavigateTo
import de.nilsdruyen.koncept.base.navigation.TopLevelRoute
import de.nilsdruyen.koncept.design.system.Icon
import de.nilsdruyen.koncept.domain.Logger.Companion.log
import de.nilsdruyen.koncept.navigation.KonceptNavHost
import de.nilsdruyen.koncept.navigation.rememberKonceptAppState
import soup.compose.material.motion.navigation.rememberMaterialMotionNavController

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalComposeUiApi::class,
    ExperimentalMaterialNavigationApi::class, ExperimentalLayoutApi::class,
)
@Composable
fun KonceptApp() {
    val useDarkIcons = !isSystemInDarkTheme()
    val systemUiController = rememberSystemUiController()

    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberMaterialMotionNavController(bottomSheetNavigator)
    val state = rememberKonceptAppState(navController)

    SideEffect {
        systemUiController.setSystemBarsColor(color = Color.Transparent, darkIcons = useDarkIcons)
    }

    ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator,
        modifier = Modifier
            .fillMaxSize()
            .semantics {
                testTagsAsResourceId = true
            }
    ) {
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
                onBackClick = state::onBackClick,
                onNavigate = { state.navigate(it) },
                modifier = Modifier
                    .padding(padding)
                    .consumeWindowInsets(padding),
            )
        }
    }
}

@Composable
fun KonceptBottomBar(
    destinations: List<TopLevelRoute>,
    onNavigateToDestination: NavigateTo,
    currentDestination: NavDestination?,
) {
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    val showBottomBar = remember(navBackStackEntry) {
//        derivedStateOf {
//            val currentDestination = navBackStackEntry?.destination
//            val dest = currentDestination ?: NavDestination("breedList")
//            dest.route in showBottomBarFor
//        }
//    }
    AnimatedVisibility(
        visible = true,
        enter = expandVertically(),
        exit = shrinkVertically(),
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
}