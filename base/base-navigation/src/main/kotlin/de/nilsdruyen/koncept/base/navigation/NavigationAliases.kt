package de.nilsdruyen.koncept.base.navigation

typealias OnNavigate = (destination: NavigationDestination) -> Unit

typealias NavigationDestination = Pair<KonceptNavDestination, String>