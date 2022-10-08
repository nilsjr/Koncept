package de.nilsdruyen.koncept.dogs.ui.navigation

import de.nilsdruyen.koncept.base.navigation.KonceptNavDestination

object BreedListDestination : KonceptNavDestination {

    const val sortTypeResult = "sortType"

    override val route = "list"
    override val destination = "breed_list_dest"
}