package de.nilsdruyen.koncept.base.navigation

import de.nilsdruyen.koncept.design.system.Icon

data class TopLevelDestination(
    override val route: String,
    override val destination: String,
    val root: String,
    val selectedIcon: Icon,
    val unselectedIcon: Icon,
    val iconTextId: Int
) : KonceptNavDestination