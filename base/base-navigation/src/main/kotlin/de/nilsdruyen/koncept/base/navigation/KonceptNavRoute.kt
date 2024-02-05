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

        fun getNestedRoute(base: GraphNavRoute?): String = if (base != null) {
            "${base.route}/$route${appendParams()}"
        } else {
            "$route${appendParams()}"
        }

        fun buildRoute(graph: GraphNavRoute, params: String): KonceptNavDestination.NestedNavDestination {
            return KonceptNavDestination.NestedNavDestination("${graph.route}/$route/$params")
        }
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
        pathParameters().map { it.name }.forEach { append("/{$it}") }
        val queryParameters = queryParameters().map { it.name }
        if (queryParameters.isNotEmpty()) {
            append("?")
            queryParameters.forEach { paramName ->
                append("$paramName={$paramName}")
            }
        }
    }
}
