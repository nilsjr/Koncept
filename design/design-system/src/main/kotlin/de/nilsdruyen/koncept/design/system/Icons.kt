package de.nilsdruyen.koncept.design.system

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Web
import androidx.compose.ui.graphics.vector.ImageVector

object KonceptIcons {

    val BreedList = Icons.Default.List
    val BreedListFilled = Icons.Filled.List
    val Favorites = Icons.Default.Favorite
    val FavoritesFilled = Icons.Filled.Favorite
    val Web = Icons.Default.Web
    val WebFilled = Icons.Filled.Web

    val FilterList = Icons.Filled.FilterList
}

sealed class Icon {
    data class ImageVectorIcon(val imageVector: ImageVector) : Icon()
    data class DrawableResourceIcon(@DrawableRes val id: Int) : Icon()
}