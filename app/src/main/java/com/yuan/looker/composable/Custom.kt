package com.yuan.looker.composable

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
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
                settings.javaScriptEnabled=true
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ) = false
                }
            }
        },
        modifier = modifier,
        update = {
            update(it)
            //it.loadUrl(url)
        }
    )
}