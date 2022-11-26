package de.nilsdruyen.koncept.base.navigation

import de.nilsdruyen.koncept.design.system.Icon

data class TopLevelRoute(
    override val route: String,
    val selectedIcon: Icon,
    val unselectedIcon: Icon,
    val iconTextId: Int
) : KonceptNavRoute.GraphNavRoute