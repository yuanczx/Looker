package com.yuan.looker.ui.screen

import android.annotation.SuppressLint
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.yuan.looker.R
import com.yuan.looker.activity.MainActivity
import com.yuan.looker.ui.theme.statusBar
import com.yuan.looker.viewmodel.NewsViewModel
import kotlinx.coroutines.delay

class ReadScreen(private val context: MainActivity) {
    private val viewModel by context.viewModels<NewsViewModel>()

    @ExperimentalAnimationApi
    @SuppressLint("SetJavaScriptEnabled")
    @Composable
    fun Screen() {
        LaunchedEffect(key1 = viewModel.lookerTheme, block = {
            delay(100)
            viewModel.loadContent()
        })
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TopBar()
            AnimatedVisibility(
                viewModel.contentLoad,
                modifier = Modifier.fillMaxSize(),
                exit = fadeOut()
            ) {
                CircularProgressIndicator(modifier = Modifier.wrapContentSize())
            }

            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = {
                    WebView(it).apply {
                        settings.apply {
                            javaScriptEnabled = true
                            javaScriptCanOpenWindowsAutomatically = true
                            loadsImagesAutomatically = true
                            blockNetworkImage = false
                            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                            //webViewClient = WebViewClient()
                        }
                    }
                },
                update = {
                    it.loadData(viewModel.currentNews, "text/html", "UTF-8")
                })

        }


    }

    @Composable
    fun TopBar() {
        TopAppBar(backgroundColor = MaterialTheme.colors.statusBar, elevation = 0.dp) {
            IconButton(onClick = {
                context.navController.popBackStack()
            }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Menu",
                    tint = Color.White,
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                )
            }

            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 20.sp,
                fontWeight = FontWeight.W500,
                color = Color.White
            )

        }
    }
}