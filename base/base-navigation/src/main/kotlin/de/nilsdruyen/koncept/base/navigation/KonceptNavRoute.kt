package de.nilsdruyen.koncept.base.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink

sealed interface KonceptNavRoute {

    val route: String

    fun pathParameters(): List<NamedNavArgument> = emptyList()

    fun queryParameters(): List<NamedNavArgument> = emptyList()

    fun deepLinks(): List<NavDeepLink> = emptyList()

    interface GraphNavRoute : KonceptNavRoute {

        fun getStartDestination() = "$route${appendParams()}"

        fun getGraphRoute(): String = "${route}_graph${appendParams()}"

        fun navigate(params: String = ""): KonceptNavDestination.TopLevelGraphDestination =
            KonceptNavDestination.TopLevelGraphDestination("$route$params")
    }

    interface NestedNavRoute : KonceptNavRoute {

//        val routePrefix
//            get() = "$baseRoute/$route"
//
//        fun getNestedRoute(): String = "$baseRoute/$route${appendParams()}"
    }
}

fun KonceptNavRoute.arguments() = pathParameters() + queryParameters()

sealed interface KonceptNavDestination {

    data class TopLevelGraphDestination(val route: String) : KonceptNavDestination

    data class GraphDestination(val route: String) : KonceptNavDestination

    data class NestedGraphDestination(val route: String) : KonceptNavDestination

    data class NestedNavDestination(val route: String) : KonceptNavDestination
}

fun KonceptNavRoute.appendParams(): String {
    return buildString {
        pathParameters().forEach { append("/{$it}") }
        val queryParameters = queryParameters()
        if (queryParameters.isNotEmpty()) {
            append("?")
            queryParameters.forEach { paramName ->
                append("$paramName={$paramName}")
            }
        }
    }
}