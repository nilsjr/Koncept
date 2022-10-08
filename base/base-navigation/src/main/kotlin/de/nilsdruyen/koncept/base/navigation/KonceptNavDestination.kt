package de.nilsdruyen.koncept.base.navigation

interface KonceptNavDestination {

    val route: String
    val destination: String

    fun createRoute(root: String) : String = "$root/$route"
}

interface KonceptScreen {

    val route: String
}