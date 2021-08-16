package com.yuan.looker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yuan.looker.ui.Screen
import com.yuan.looker.ui.compose.MainScreen
import com.yuan.looker.ui.compose.SecondScreen
import com.yuan.looker.ui.compose.TestScreen
import com.yuan.looker.ui.theme.LookerTheme

class MainActivity : ComponentActivity() {
    private val dataStore:DataStore<Preferences> by preferencesDataStore("settings")
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor= 0xFF16A085.toInt()
        setContent {
            val navController = rememberNavController()
            LookerTheme() {
                NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
                    composable(Screen.MainScreen.route) { MainScreen(navController = navController) }
                    composable(Screen.SecondScreen.route) { SecondScreen() }
                    composable(Screen.TestScreen.route) { TestScreen(dataStore = dataStore) }
                }
            }
        }

    }

}
