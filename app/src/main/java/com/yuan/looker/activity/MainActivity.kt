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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.yuan.looker.ui.screen.MainScreen
import com.yuan.looker.ui.screen.ReadScreen
import com.yuan.looker.ui.screen.SettingScreen
import com.yuan.looker.ui.theme.DarkColorPalette
import com.yuan.looker.ui.theme.LookerTheme
import com.yuan.looker.ui.theme.statusBar
import com.yuan.looker.utils.sealed.Screen
import com.yuan.looker.viewmodel.NewsViewModel
import com.yuan.looker.viewmodel.NewsViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


val MainActivity.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
var MainActivity.splash: Boolean by mutableStateOf(false)

class MainActivity : ComponentActivity(), CoroutineScope by MainScope() {
    lateinit var navController: NavHostController
    private val viewModel:NewsViewModel by viewModels{NewsViewModelFactory(this)}

    //初始化设置
    init {
        launch {
            //深色模式跟随系统
            viewModel.darkMode= dataStore.data.first()[booleanPreferencesKey("darkMode")]?: false
            viewModel.themeIndex= dataStore.data.first()[intPreferencesKey("theme")] ?: 0
            viewModel.lookerTheme = viewModel.loadTheme()
        }
    }

    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    @ExperimentalCoilApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //WindowCompat.setDecorFitsSystemWindows(window, false) 取消状态栏占位

        //加载新闻
        launch { viewModel.loadNews(0) }
        setContent {
            //深色模式设置
            if (viewModel.darkMode ){
                if (isSystemInDarkTheme()) {
                    viewModel.lookerTheme = DarkColorPalette
                }
            }else{
                viewModel.lookerTheme = viewModel.loadTheme()
            }
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
                    viewModel.load = false
                    //设置状态栏颜色
                    window.statusBarColor = MaterialTheme.colors.statusBar.toArgb()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.MainScreen.route,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        composable(Screen.MainScreen.route) { mainScreen.Screen() }
                        composable(Screen.ReadScreen.route){ readScreen.Screen()}
                        composable(Screen.SettingScreen.route) { settingScreen.Screen() }
                    }
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}

