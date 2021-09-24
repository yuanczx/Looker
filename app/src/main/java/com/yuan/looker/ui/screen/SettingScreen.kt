package com.yuan.looker.ui.screen

import android.annotation.SuppressLint
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.yuan.looker.R
import com.yuan.looker.activity.MainActivity
import com.yuan.looker.activity.dataStore
import com.yuan.looker.composable.Setting
import com.yuan.looker.ui.theme.*
import com.yuan.looker.viewmodel.NewsViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class SettingScreen(private val context: MainActivity) {
    private val viewModel by context.viewModels<NewsViewModel>()

    @SuppressLint("CoroutineCreationDuringComposition")
    @ExperimentalAnimationApi
    @Composable
    fun Screen() {
        val settingUtils = Setting(context)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(if (viewModel.lookerTheme == DarkColorPalette) Color.Black else Color.White)
        ) {
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

            val nightModeKey = booleanPreferencesKey("darkMode")
            settingUtils.Switcher(
                key = nightModeKey,
                title = "深色模式",
                icon = rememberVectorPainter(image = Icons.Outlined.Email),
                label = "是否开启深色模式",
                itemClick = {
                    viewModel.lookerTheme = if (it) {
                        DarkColorPalette
                    } else {
                        when (context.dataStore.data.first()[intPreferencesKey("theme")]
                            ?: 0) {
                            0 -> BlueTheme
                            1 -> OrangeTheme
                            2 -> GreenTheme
                            3 -> PurpleTheme
                            else -> BlueTheme
                        }
                    }
                }
            )


            val themeSelector = listOf("蓝色", "橙色", "青色", "紫色")
            val themeKey = intPreferencesKey("theme")


            settingUtils.Selector(
                key = themeKey,
                title = "主题管理",
                icon = painterResource(id = R.drawable.ic_theme),
                label = "选择你喜欢的颜色",
                data = themeSelector,
                iconSpaceReserve = true,
                itemClick = { index ->
                    context.launch {
                        viewModel.lookerTheme = when (index) {
                            0 -> BlueTheme
                            1 -> OrangeTheme
                            2 -> GreenTheme
                            3 -> PurpleTheme
                            else -> BlueTheme
                        }
                        context.dataStore.edit {
                            it[nightModeKey] = false
                        }
                    }
                }
            )


            val editorKey = stringPreferencesKey("editor")
            settingUtils.Editor(
                key = editorKey,
                title = "Test it is success?",
                label = "Hello world",
                icon = rememberVectorPainter(image = Icons.Outlined.Search)
            )
        }
    }
}