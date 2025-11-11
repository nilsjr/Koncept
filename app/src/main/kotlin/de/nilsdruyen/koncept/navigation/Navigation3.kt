package de.nilsdruyen.koncept.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import de.nilsdruyen.koncept.base.navigation.EntryProviderInstaller
import de.nilsdruyen.koncept.base.navigation.Navigator

@Composable
fun Navigation3(
    navigator: Navigator,
    entryProviderScopes: Set<@JvmSuppressWildcards EntryProviderInstaller>,
    modifier: Modifier = Modifier
) {
    NavDisplay(
        backStack = navigator.backStack,
        modifier = modifier,
        onBack = { navigator.goBack() },
        entryProvider = entryProvider {
            entryProviderScopes.forEach { builder -> this.builder() }
        }
    )
}