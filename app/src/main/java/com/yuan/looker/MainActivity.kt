package com.yuan.looker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yuan.looker.ui.Screen
import com.yuan.looker.ui.screen.MainScreen
import com.yuan.looker.ui.screen.SettingScreen
import com.yuan.looker.ui.theme.LightColorPalette
import com.yuan.looker.ui.theme.LookerTheme
import com.yuan.looker.ui.theme.statusBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel


val MainActivity.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

class MainActivity : ComponentActivity(), CoroutineScope by MainScope() {
    lateinit var settingScreen: SettingScreen
    lateinit var navController: NavController
    lateinit var myTheme: MutableState<Colors>
    var job: Job? = null

    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            navController = rememberNavController()
            val mainScreen = MainScreen(this)
            settingScreen = SettingScreen(this)
            myTheme = remember { mutableStateOf(LightColorPalette) }
            LookerTheme(myTheme.value) {
                window.statusBarColor = MaterialTheme.colors.statusBar.toArgb()
                NavHost(
                    navController = navController as NavHostController,
                    startDestination = Screen.MainScreen.route
                ) {
                    composable(Screen.MainScreen.route) { mainScreen.Screen() }
                    composable(Screen.SettingScreen.route) { settingScreen.Screen() }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

}
