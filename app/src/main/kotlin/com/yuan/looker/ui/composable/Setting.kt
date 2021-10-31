package com.yuan.looker.ui.composable

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
            modifier = Modifier.padding(end = 10.dp),
            onCheckedChange = { it ->
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
    var visible by remember {
        mutableStateOf(false)
    }
    var text by remember {
        mutableStateOf("")
    }
    context.launch {
        text = context.dataStore.data.first()[key] ?: ""
    }
    Column {
        BasicSetting(
            icon = icon,
            title = title,
            label = label,
            iconSpaceReserve = iconSpaceReserve,
            itemClick = {
                visible = !visible
                itemClick()
            }) {}
        AnimatedVisibility(visible = visible) {
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
    }
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
    //Modifier
) {
    val visibility = remember {
        mutableStateOf(false)
    }

    var item by remember {
        mutableStateOf(0)
    }

    context.launch {
        item = context.dataStore.data.first()[key] ?: 0
        cancel()
    }


    Column {
        BasicSetting(
            icon = icon,
            title = title,
            label = label,
            iconSpaceReserve = iconSpaceReserve,
            content = {

                Spacer(modifier = Modifier.weight(0.7f))
                OutlinedButton(
                    modifier = Modifier
                        .height(30.dp)
                        .padding(end = 10.dp),
                    border = BorderStroke(2.dp, MaterialTheme.colors.primary),
                    shape = RoundedCornerShape(45),
                    onClick = {}) {
                    Text(
                        text = data[item],
                        fontSize = 13.sp,
                        softWrap = true,
                        maxLines = 1
                    )
                }
            },
            itemClick = { visibility.value = !visibility.value })

        AnimatedVisibility(visible = visibility.value) {
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
    }
}

//通用Compose
