package de.nilsdruyen.koncept.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import de.nilsdruyen.koncept.common.ui.KonceptTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            AppRoot()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AppRoot() {
    KonceptTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .semantics {
                    testTagsAsResourceId = true
                }
        ) {
            KonceptApp()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KonceptTheme {
        KonceptApp()
    }
}