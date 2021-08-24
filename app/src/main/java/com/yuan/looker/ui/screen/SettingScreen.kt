package com.yuan.looker.ui.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.yuan.looker.MainActivity
import com.yuan.looker.R
import com.yuan.looker.composable.Setting
import com.yuan.looker.lookerTheme
import com.yuan.looker.ui.theme.BlueTheme
import com.yuan.looker.ui.theme.GreenTheme
import com.yuan.looker.ui.theme.OrangeTheme
import com.yuan.looker.ui.theme.PurpleTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SettingScreen(private val context: MainActivity) {

    @ExperimentalAnimationApi
    @Composable
    fun Screen() {
        val settingUtils = Setting(context)
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
                Text(text = "设置", fontWeight = W600, fontSize = 20.sp)
            }

            settingUtils.Switch(
                key = booleanPreferencesKey("theme"),
                title = "DarkTheme",
                icon = rememberVectorPainter(image = Icons.Outlined.Email),
                label = "On Dark Theme"
            )
            val themeSelector = listOf("蓝色", "橙色", "青色","紫色")
            val themeKey = intPreferencesKey("select")
            settingUtils.Selector(
                key = themeKey,
                title = "主题管理",
                icon = painterResource(id = R.drawable.ic_theme),
                label = "选择你喜欢的颜色",
                data = themeSelector,
                iconSpaceReserve = true,
                itemClick = { index ->
                    context.launch {
                        delay(10)
                        context.lookerTheme = when (index) {
                            0 -> BlueTheme
                            1 -> OrangeTheme
                            2 -> GreenTheme
                            3 -> PurpleTheme
                            else -> BlueTheme
                        }
                    }


                }
            )
            val editorKey = stringPreferencesKey("editor")
            settingUtils.Editor(
                key = editorKey,
                title = "Test it is success?",
                label = "Hello world",
                icon = rememberVectorPainter(
                    image = Icons.Outlined.Search
                )
            )

        }
    }
}