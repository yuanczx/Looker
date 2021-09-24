package com.yuan.looker.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.yuan.looker.ui.Screen
import com.yuan.looker.ui.screen.MainScreen
import com.yuan.looker.ui.screen.ReadScreen
import com.yuan.looker.ui.screen.SettingScreen
import com.yuan.looker.ui.theme.*
import com.yuan.looker.viewmodel.NewsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


val MainActivity.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
var MainActivity.splash: Boolean by mutableStateOf(false)
//var MainActivity.lookerTheme by mutableStateOf(BlueTheme)


class MainActivity : ComponentActivity(), CoroutineScope by MainScope() {
    lateinit var navController: NavHostController
    private val viewModel by viewModels<NewsViewModel>()

    //初始化设置
    init {
        launch {
            val themeIndex = dataStore.data.first()[intPreferencesKey("theme")] ?: 0
            viewModel.lookerTheme = when (themeIndex) {
                0 -> BlueTheme
                1 -> OrangeTheme
                2 -> GreenTheme
                3 -> PurpleTheme
                else -> DarkColorPalette
            }
        }
    }

    @ExperimentalCoilApi
    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //WindowCompat.setDecorFitsSystemWindows(window, false) 取消状态栏占位
        window.statusBarColor = Blue500.toArgb()

        //加载新闻
        launch {
            viewModel.loadNews(0)
        }

        setContent {
            //变量声明
            navController = rememberNavController()

            val mainScreen = MainScreen(this)
            val settingScreen = SettingScreen(this)
            val readScreen = ReadScreen(this)

            //Compose界面
            mainScreen.Splash(splash)
            AnimatedVisibility(
                visible = splash,
                enter = fadeIn(animationSpec = tween(durationMillis = 500)),
            ) {
                LookerTheme(theme = viewModel.lookerTheme) {
                    //设置状态栏颜色
                    window.statusBarColor = MaterialTheme.colors.statusBar.toArgb()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.MainScreen.route,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        composable(Screen.MainScreen.route) { mainScreen.Screen() }
                        composable(Screen.ReadScreen.route){readScreen.Screen()}
                        composable(Screen.SettingScreen.route) { settingScreen.Screen() }
                    }
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        //取消MainScope
        cancel()
    }
}
