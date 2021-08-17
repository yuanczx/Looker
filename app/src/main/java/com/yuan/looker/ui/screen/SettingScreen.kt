package com.yuan.looker.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.yuan.looker.MainActivity
import com.yuan.looker.ui.SettingUtils


class SettingScreen(private val context: MainActivity) {

    @Composable
    fun Screen() {
        val settingUtils = SettingUtils(context.getDs(), rememberCoroutineScope())
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar {
                IconButton(onClick = {
                    context.navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Text(text = "设置", fontWeight = W600)
            }
            settingUtils.SettingSwitch(
                key = booleanPreferencesKey("theme"),
                title = "DarkTheme",
                icon = Icons.Outlined.Email,
                label = "On Dark Theme"
            )
        }
    }
}