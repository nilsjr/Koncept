package de.nilsdruyen.koncept.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import de.nilsdruyen.koncept.base.navigation.EntryProviderInstaller
import de.nilsdruyen.koncept.base.navigation.Navigator
import de.nilsdruyen.koncept.design.system.KonceptTheme
import de.nilsdruyen.koncept.navigation.Navigation3
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    @Inject
//    lateinit var navigator: Navigator

//    @Inject
//    lateinit var entryProviderScopes: Set<@JvmSuppressWildcards EntryProviderInstaller>

    private val isDone: MutableState<Boolean> = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        installSplashScreen().setKeepOnScreenCondition { !isDone.value }
        super.onCreate(savedInstanceState)

        setContent {
            LaunchedEffect(Unit) {
                // sync some stuff that is needed
                isDone.value = true
            }

            KonceptTheme {
                KonceptApp()
//                    navigator = navigator,
//                    entryProviderScopes = entryProviderScopes,
//                )
            }
        }
    }
}
