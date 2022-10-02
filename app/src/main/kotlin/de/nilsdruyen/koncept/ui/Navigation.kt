package de.nilsdruyen.koncept.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import de.nilsdruyen.koncept.base.navigation.TopLevelDestination
import de.nilsdruyen.koncept.design.system.Icon
import de.nilsdruyen.koncept.navigation.KonceptNavigation
import soup.compose.material.motion.navigation.rememberMaterialMotionNavController

@OptIn(
    ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun KonceptApp() {
    val navController = rememberMaterialMotionNavController()
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()
    val state = rememberKonceptAppState(navController)

    SideEffect {
        systemUiController.setSystemBarsColor(color = Color.Transparent, darkIcons = useDarkIcons)
    }

    Scaffold(
        modifier = Modifier.semantics {
            testTagsAsResourceId = true
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            KonceptBottomBar(
                destinations = state.topLevelDestinations,
                onNavigateToDestination = state::navigate,
                currentDestination = state.currentDestination,
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal
                    )
                )
        ) {
            KonceptNavigation(
                navController = navController,
                modifier = Modifier
                    .padding(padding)
                    .consumedWindowInsets(padding),
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
            modifier = Modifier,
            backgroundColor = MaterialTheme.colorScheme.surface,
        ) {
            destinations.forEach { item ->
                val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                val alpha by animateFloatAsState(targetValue = if (isSelected) 1f else .7f)

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
                    label = {
                        Text(text = stringResource(id = item.iconTextId), modifier = Modifier.alpha(alpha))
                    },
                    modifier = Modifier.testTag(item.route)
                )
            }
        }
    }
}