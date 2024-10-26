package de.nilsdruyen.koncept.navigation

import de.nilsdruyen.koncept.base.navigation.KonceptNavRoute
import kotlinx.serialization.Serializable

@Serializable
object WebRoute : KonceptNavRoute.GraphNavRoute {

    override val route = "web"
}
