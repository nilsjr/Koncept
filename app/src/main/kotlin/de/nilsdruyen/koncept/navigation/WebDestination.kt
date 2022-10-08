package de.nilsdruyen.koncept.navigation

import de.nilsdruyen.koncept.base.navigation.KonceptNavDestination

object WebTopLevel {

    const val root = "web"
}
object WebDestination : KonceptNavDestination {

    override val route = "web"
    override val destination = "web_dest"
}