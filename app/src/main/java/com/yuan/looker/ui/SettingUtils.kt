package com.yuan.looker.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.yuan.looker.MainActivity
import com.yuan.looker.dataStore
import com.yuan.looker.ui.theme.Gray300
import com.yuan.looker.ui.theme.Gray500
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class SettingUtils(
    private val context: MainActivity
) {
    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    fun Switch(
        modifier: Modifier = Modifier,//Modifier
        key: Preferences.Key<Boolean>,//DataStore key
        title: String = "",//标题
        icon: ImageVector? = null,//图标
        label: String? = null//标签

    ) {
        //定义变量
        val scope = rememberCoroutineScope()
        var switch by remember {
            mutableStateOf(false)
        }
        //获取DataStore数据
        context.launch {
            switch = context.dataStore.data.first()[key] ?: false
            cancel()
        }

        //Compose界面
        BasicSetting(icon, title, label, modifier, itemClick = {
            switch = !switch
            setData(key, switch)
        }) {
            Switch(
                checked = switch,
                modifier = Modifier.padding(end = 5.dp),
                onCheckedChange = {
                    switch = it
                    setData(key, it)
                },
                colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colors.primary)
            )
        }
        //开关

    }


    @ExperimentalAnimationApi
    @Composable
    fun Selector(
        modifier: Modifier = Modifier,
        key: Preferences.Key<Int>,//DataStore key
        title: String = "",//标题
        icon: ImageVector? = null,//图标
        label: String? = null,//标签
        //Modifier
    ) {
        BasicSetting(icon = icon, title = title, label = label, modifier = modifier) {
            AnimatedVisibility(visible = false) {
                repeat(10) {
                    RadioButton(selected = false, onClick = { /*TODO*/ })
                }

            }

        }
    }
    //通用Compose

    @Composable
    private fun BasicSetting(
        icon: ImageVector?,
        title: String,
        label: String?,
        modifier: Modifier,
        itemClick: (() -> Unit) = {},
        content: @Composable () -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clickable {
                    itemClick()
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(contentAlignment = Alignment.CenterStart) {
                //图标
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = title,
                        modifier = Modifier
                            .size(42.dp)
                            .padding(start = 10.dp),
                        tint = Gray300
                    )
                }
                //文字
                Column(
                    modifier = Modifier.padding(start = 55.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "This is a Test",
                        fontSize = 16.sp,
                        color = Gray500,
                        fontWeight = FontWeight.W500
                    )
                    label?.let {
                        Text(text = it, fontSize = 13.sp, color = Gray300)
                    }
                }

            }
            content()
        }
    }


    //获取DataStore数据
    private fun setData(key: Preferences.Key<Boolean>, value: Boolean) {
        context.launch {
            context.dataStore.edit { it[key] = value }
            cancel()
        }
    }


}
