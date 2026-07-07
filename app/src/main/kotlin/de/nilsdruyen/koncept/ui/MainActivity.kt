package de.nilsdruyen.koncept.ui

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import de.nilsdruyen.koncept.design.system.KonceptTheme
import de.nilsdruyen.koncept.navigation.DeeplinkRoute

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val isDone: MutableState<Boolean> = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        installSplashScreen().setKeepOnScreenCondition { !isDone.value }
        super.onCreate(savedInstanceState)

        val deeplink = intent?.data?.toDeeplinkRoute()

        setContent {
            LaunchedEffect(Unit) {
                // sync some stuff that is needed
                isDone.value = true
            }

            KonceptTheme {
                KonceptApp(deeplink = deeplink)
            }
        }
    }
}

private fun Uri.toDeeplinkRoute(): DeeplinkRoute? {
    if (scheme != "koncept" || host != "deeplink") return null
    val rawDate = pathSegments.firstOrNull() ?: return null

    return DeeplinkRoute(rawDate = rawDate, rawDate2 = getQueryParameter("rawDate2").orEmpty())
}
