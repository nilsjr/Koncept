package de.nilsdruyen.koncept.ui

import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WebScreen(modifier: Modifier = Modifier) {
//    BoxWithConstraints(
//        modifier
//            .imePadding()
//            .imeNestedScroll()
//    ) {
//        // WebView changes it's layout strategy based on
//        // it's layoutParams. We convert from Compose Modifier to
//        // layout params here.
//        val width =
//            if (constraints.hasFixedWidth)
//                FrameLayout.LayoutParams.MATCH_PARENT
//            else
//                FrameLayout.LayoutParams.WRAP_CONTENT
//        val height =
//            if (constraints.hasFixedHeight)
//                FrameLayout.LayoutParams.MATCH_PARENT
//            else
//                FrameLayout.LayoutParams.WRAP_CONTENT
//
//        val definedLayoutParams = FrameLayout.LayoutParams(
//            width,
//            height
//        )
//
//        AndroidView(
//            modifier = Modifier.fillMaxSize(),
//            factory = { context ->
//                WebView(context).apply {
//                    layoutParams = definedLayoutParams
//                    webViewClient = WebViewClient()
//                    settings.javaScriptEnabled = true
//                }
//            },
//            update = { webView -> webView.loadUrl("file:///android_asset/index.html") }
//        )
//    }

    val state = rememberWebViewState("file:///android_asset/index.html")

    WebView(
        state = state,
        modifier = modifier
            .imePadding()
            .imeNestedScroll()
    )
}