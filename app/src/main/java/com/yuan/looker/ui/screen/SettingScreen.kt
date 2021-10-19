package com.yuan.looker.ui.screen

import android.annotation.SuppressLint
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import com.yuan.looker.R
import com.yuan.looker.activity.MainActivity
import com.yuan.looker.ui.composable.Setting
import com.yuan.looker.ui.theme.statusBar
import com.yuan.looker.viewmodel.NewsViewModel


class SettingScreen(private val context: MainActivity) {
    private val viewModel by context.viewModels<NewsViewModel>()

    @SuppressLint("CoroutineCreationDuringComposition")
    @ExperimentalAnimationApi
    @Composable
    fun Screen() {
        val settingUtils = Setting(context)
        Column(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)) {
            TopAppBar(backgroundColor = MaterialTheme.colors.statusBar) {
                IconButton(onClick = {
                    context.navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(text = stringResource(R.string.settings), fontWeight = W600, fontSize = 20.sp,color = Color.White)
            }
            val nightModeKey = booleanPreferencesKey("darkMode")
            settingUtils.Switcher(
                key = nightModeKey,
                title = stringResource(R.string.dark_mode),
                icon = painterResource(id = R.drawable.ic_invert_colors),
                label = stringResource(R.string.follow_system),
                itemClick = {
                    viewModel.darkMode = it
                }
            )


            val themeSelector = viewModel.arrayRes(R.array.theme)
            val themeKey = intPreferencesKey("theme")

            settingUtils.Selector(
                key = themeKey,
                itemClick = { index ->
                    viewModel.themeIndex = index
                    if (viewModel.darkMode) return@Selector
                    viewModel.lookerTheme =  viewModel.loadTheme()
                },
                title = stringResource(R.string.theme_manage),
                icon = painterResource(id = R.drawable.ic_theme),
                label = stringResource(R.string.choose_color),
                data = themeSelector,
                iconSpaceReserve = true
            )
//            val editorKey = stringPreferencesKey("editor")
//            settingUtils.Editor(
//                key = editorKey,
//                title = "Test it is success?",
//                label = "Hello world",
//                icon = rememberVectorPainter(image = Icons.Outlined.Search)
//            )
        }
    }
}