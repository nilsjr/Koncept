package de.nilsdruyen.koncept.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import de.nilsdruyen.koncept.design.system.KonceptTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            AppRoot()
        }
    }
}

@Composable
fun AppRoot() {
    KonceptTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
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