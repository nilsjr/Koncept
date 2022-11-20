package de.nilsdruyen.koncept.base.navigation

sealed interface KonceptNavRoute {

    val route: String

    fun pathParameters(): List<String> = emptyList()

    fun queryParameters(): List<String> = emptyList()

    interface GraphNavRoute : KonceptNavRoute {

        fun getStartDestination() = "$route${appendParams()}"

        fun getGraphRoute(): String = "${route}_graph${appendParams()}"

        fun navigate(params: String = ""): KonceptNavDestination.TopLevelGraphDestination =
            KonceptNavDestination.TopLevelGraphDestination("$route$params")
    }

    abstract class NestedNavRoute(private val baseRoute: String) : KonceptNavRoute {

        val routePrefix
            get() = "$baseRoute/$route"

        fun getNestedRoute(): String = "$baseRoute/$route${appendParams()}"
    }
}

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