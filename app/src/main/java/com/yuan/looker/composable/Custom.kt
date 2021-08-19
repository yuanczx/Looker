package com.yuan.looker.composable

import android.content.Context
import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebCompo(
    modifier: Modifier=Modifier,
    context: Context,
    url: String,
    update: (WebView) -> Unit = {}
) {
    AndroidView(
        factory = {
            WebView(context).apply {
                loadUrl(url)
            }
        },
        modifier = modifier,
        update = {
            //it.loadUrl(url)
        }
    )
}