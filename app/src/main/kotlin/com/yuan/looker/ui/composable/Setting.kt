package com.yuan.looker.ui.composable

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.yuan.looker.activity.MainActivity
import com.yuan.looker.activity.dataStore
import com.yuan.looker.ui.theme.Gray500
import com.yuan.looker.ui.theme.settingBg
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Switcher(
    context: MainActivity,
    key: Preferences.Key<Boolean>,//DataStore key
    title: String = "",//标题
    icon: Painter? = null,//图标
    label: String? = null,//标签
    itemClick: (suspend (Boolean) -> Unit)? = null
) {
    //定义变量
    var switch by remember { mutableStateOf(false) }
    //获取DataStore数据
    context.launch {
        switch = context.dataStore.data.first()[key] ?: false
    }
    //Compose界面
    BasicSetting(icon, title, label, itemClick = {
        context.launch {
            switch = !switch
            context.dataStore.edit {
                it[key] = switch
            }

        }
    }) {
        Switch(
            checked = switch,
            onCheckedChange = {
                switch = it
                context.launch {
                    context.dataStore.edit { data ->
                        data[key] = switch
                    }
                }
            },
            colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colors.primary)
        )
    }
    //开关
    LaunchedEffect(key1 = switch, block = {
        context.launch {
            itemClick?.let {
                it(switch)
            }
        }
    })
}

@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalAnimationApi
@Composable
fun Editor(
    context: MainActivity,
    key: Preferences.Key<String>,
    title: String,
    icon: Painter? = null,
    label: String? = null,
    iconSpaceReserve: Boolean = true,
    itemClick: () -> Unit = {}
) {
    var text by remember {
        mutableStateOf("")
    }
    context.launch {
        text = context.dataStore.data.first()[key] ?: ""
    }


    @Composable
    fun SettingEditor() {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(MaterialTheme.colors.settingBg),
            value = text,
            singleLine = true,
            onValueChange = {
                text = it
                context.launch {
                    context.dataStore.edit { data ->
                        data[key] = it
                    }
                }
            })
    }
    BasicSetting(
        icon = icon,
        title = title,
        label = label,
        iconSpaceReserve = iconSpaceReserve,
        expand = { SettingEditor() },
        itemClick = {
            itemClick()
        })
}


@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalAnimationApi
@Composable
fun Selector(
    context: MainActivity,
    key: Preferences.Key<Int>,//DataStore key
    title: String,//标题
    data: Array<String>,//数据
    icon: Painter? = null,//图标
    iconSpaceReserve: Boolean = true,
    label: String? = null,//标签
    itemClick: (Int) -> Unit = {}//标签
) {

    var item by remember { mutableStateOf(0) }

    context.launch {
        item = context.dataStore.data.first()[key] ?: 0
        cancel()
    }
    @Composable
    fun SettingSelector() {
        Column(Modifier.background(MaterialTheme.colors.settingBg)) {
            fun rowClick(index: Int) {
                item = index
                context.launch {
                    context.dataStore.edit { it[key] = index }
                    itemClick(index)
                }
            }
            repeat(data.size) { index ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clickable {
                            rowClick(index)
                        },
                    //horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                        selected = index == item,
                        onClick = {
                            rowClick(index)
                        })
                    Text(
                        modifier = Modifier.padding(end = 10.dp),
                        text = data[index],
                        color = Gray500
                    )
                }

            }
        }

    }
    BasicSetting(
        icon = icon,
        title = title,
        label = label,
        iconSpaceReserve = iconSpaceReserve,
        expand = { SettingSelector() },
        content = {
            Box(
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(45.dp)
                    )

            ) {
                Text(
                    modifier = Modifier.padding(
                        start = 10.dp,
                        end = 10.dp,
                        top = 6.dp,
                        bottom = 6.dp
                    ),
                    color = MaterialTheme.colors.primary,
                    text = data[item],
                    fontSize = 12.sp,
                    softWrap = true,
                    maxLines = 1
                )
            }
        },
        itemClick = { itemClick(item) })


}

//@Composable
//fun Slider(
//    context: MainActivity,
//    key: Preferences.Key<Float>,
//    title: String,
//    icon: Painter? = null,
//    iconSpaceReserve: Boolean = true,
//    label: String? = null,
//    itemClick: () -> Unit
//) {
//    BasicSetting(icon = icon, title = title, label = label, iconSpaceReserve = iconSpaceReserve)
//}