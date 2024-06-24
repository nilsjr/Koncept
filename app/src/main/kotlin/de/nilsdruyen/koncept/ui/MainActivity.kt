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
import de.nilsdruyen.koncept.design.system.KonceptTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

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
            }
        }
    }
}
