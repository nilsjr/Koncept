package de.nilsdruyen.koncept.design.system

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Web
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Web
import androidx.compose.ui.graphics.vector.ImageVector

object KonceptIcons {

    val BreedList = Icons.AutoMirrored.Outlined.List
    val BreedListFilled = Icons.AutoMirrored.Filled.List
    val Favorites = Icons.Outlined.Favorite
    val FavoritesFilled = Icons.Filled.Favorite
    val Web = Icons.Outlined.Web
    val WebFilled = Icons.Filled.Web

    val FilterList = Icons.Default.FilterList
}

sealed class Icon {
    data class ImageVectorIcon(val imageVector: ImageVector) : Icon()
    data class DrawableResourceIcon(@DrawableRes val id: Int) : Icon()
}
