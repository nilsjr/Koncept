package de.nilsdruyen.koncept.ui

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebScreen() {
    OwnWebView(Modifier)
//    AccompanistWebView(modifier)
}

@Composable
fun OwnWebView(modifier: Modifier) {
    AndroidView(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .imePadding(),
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
            }
        },
        update = { webView -> webView.loadUrl("file:///android_asset/index.html") }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AccompanistWebView(modifier: Modifier) {
//    val state = rememberWebViewState("file:///android_asset/index.html")
    val state = rememberWebViewState("https://www.google.de")

    WebView(
        state = state,
        modifier = modifier
            .statusBarsPadding()
            .imePadding()
            .imeNestedScroll()
    )
}
