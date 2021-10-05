package com.yuan.looker.ui.screen

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.yuan.looker.activity.MainActivity
import com.yuan.looker.viewmodel.NewsViewModel

class ReadScreen(private val context: MainActivity) {
    val viewModel by context.viewModels<NewsViewModel>()

    @SuppressLint("SetJavaScriptEnabled")
    @Composable
    fun Screen() {


        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
            WebView(it).apply {
                settings.apply {
                    javaScriptEnabled = true
                    javaScriptCanOpenWindowsAutomatically = true
                    loadsImagesAutomatically = true
                    webViewClient = WebViewClient()
                }
                loadUrl(viewModel.currentUrl)
            }
        },)


    }
}