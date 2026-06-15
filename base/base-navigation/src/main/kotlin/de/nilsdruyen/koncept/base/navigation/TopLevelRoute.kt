package de.nilsdruyen.koncept.base.navigation

import androidx.navigation3.runtime.NavKey
import de.nilsdruyen.koncept.design.system.Icon

data class TopLevelRoute(val route: NavKey, val selectedIcon: Icon, val unselectedIcon: Icon, val iconTextId: Int)
