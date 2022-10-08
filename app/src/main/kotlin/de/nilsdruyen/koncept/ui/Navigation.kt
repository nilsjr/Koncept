package de.nilsdruyen.koncept.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import de.nilsdruyen.koncept.base.navigation.TopLevelDestination
import de.nilsdruyen.koncept.design.system.Icon
import de.nilsdruyen.koncept.navigation.KonceptNavigation
import de.nilsdruyen.koncept.navigation.rememberKonceptAppState
import soup.compose.material.motion.navigation.rememberMaterialMotionNavController

@OptIn(
    ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class, ExperimentalMaterialNavigationApi::class
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
        modifier = Modifier.semantics {
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
            KonceptNavigation(
                navController = navController,
                modifier = Modifier.padding(padding),
                onBackClick = state::onBackClick,
                onNavigate = {
                    state.navigate(it.first, it.second)
                },
            )
        }
    }
}

@Composable
fun KonceptBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
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
        BottomNavigation(
            backgroundColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.navigationBarsPadding()
        ) {
            destinations.forEach { item ->
                val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                BottomNavigationItem(
                    selected = isSelected,
                    onClick = { onNavigateToDestination(item) },
                    icon = {
                        val icon = if (isSelected) {
                            item.selectedIcon
                        } else {
                            item.unselectedIcon
                        }
                        when (icon) {
                            is Icon.ImageVectorIcon -> androidx.compose.material3.Icon(
                                imageVector = icon.imageVector,
                                contentDescription = null
                            )

                            is Icon.DrawableResourceIcon -> androidx.compose.material3.Icon(
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