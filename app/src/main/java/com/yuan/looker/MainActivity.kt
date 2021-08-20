package com.yuan.looker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.yuan.looker.ui.Screen
import com.yuan.looker.ui.screen.MainScreen
import com.yuan.looker.ui.screen.SettingScreen
import com.yuan.looker.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


val MainActivity.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
var MainActivity.splash: Boolean by mutableStateOf(false)
var MainActivity.lookerTheme by mutableStateOf(LightColorPalette)


class MainActivity : ComponentActivity(), CoroutineScope by MainScope() {


    //var theme = mutableStateOf(LightColorPalette)
    lateinit var navController: NavHostController

    //初始化设置
    init {
        launch {
            val themeIndex = dataStore.data.first()[intPreferencesKey("select")] ?: 0
            lookerTheme = when (themeIndex) {
                0 -> LightColorPalette
                1 -> OrangeTheme
                2 -> BlueTheme
                3 -> PurpleTheme
                else -> LightColorPalette
            }
        }
    }

    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //WindowCompat.setDecorFitsSystemWindows(window, false) 取消状态栏占位
        window.statusBarColor = Blue500.toArgb()
        setContent {
            //变量声明
            navController = rememberNavController()
            val mainScreen = MainScreen(this)
            val settingScreen = SettingScreen(this)

            //Compose界面
            mainScreen.Splash(splash)
            AnimatedVisibility(
                visible = splash,
                enter = fadeIn(animationSpec = tween(durationMillis = 500)),
            ) {
                LookerTheme(lookerTheme) {
                    //设置状态栏颜色
                    window.statusBarColor = MaterialTheme.colors.statusBar.toArgb()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.MainScreen.route,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        composable(Screen.MainScreen.route) { mainScreen.Screen() }
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

