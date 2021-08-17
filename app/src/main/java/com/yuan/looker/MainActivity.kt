package com.yuan.looker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yuan.looker.ui.Screen
import com.yuan.looker.ui.screen.MainScreen
import com.yuan.looker.ui.theme.LightColorPalette
import com.yuan.looker.ui.theme.LookerTheme

class MainActivity : ComponentActivity() {
    public lateinit var myTheme:MutableState<Colors>
    private val dataStore: DataStore<Preferences> by preferencesDataStore("settings")
    val mainScreen = MainScreen(this)
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            //WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val navController = rememberNavController()
            myTheme = remember { mutableStateOf(LightColorPalette) }
              LookerTheme(myTheme.value) {
                window.statusBarColor = MaterialTheme.colors.primary.toArgb()
                  NavHost(
                        navController = navController,
                        startDestination = Screen.MainScreen.route
                    ) {
                        composable(Screen.MainScreen.route) { mainScreen.Screen(navController = navController) }
                        composable(Screen.SecondScreen.route) { mainScreen.SecondScreen() }
                        composable(Screen.TestScreen.route) { mainScreen.TestScreen(dataStore = dataStore) }
                    }
            }
        }
    }
}
