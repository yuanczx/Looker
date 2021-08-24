package com.yuan.looker.composable

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.yuan.looker.MainActivity
import com.yuan.looker.dataStore
import com.yuan.looker.ui.theme.Gray300
import com.yuan.looker.ui.theme.Gray500
import com.yuan.looker.ui.theme.settingBg
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class Setting(private val context: MainActivity) {
    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    fun Switch(
        key: Preferences.Key<Boolean>,//DataStore key
        title: String = "",//标题
        icon: Painter? = null,//图标
        label: String? = null//标签

    ) {
        //定义变量
        var switch by remember {
            mutableStateOf(false)
        }
        //获取DataStore数据
        context.launch {
            switch = readData(key)
            cancel()
        }

        //Compose界面
        BasicSetting(icon, title, label, itemClick = {
            switch = !switch
            writeData(key, switch)
        }) {
            Switch(
                checked = switch,
                modifier = Modifier.padding(end = 10.dp),
                onCheckedChange = {
                    switch = it
                    writeData(key, it)
                },
                colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colors.primary)
            )
        }
        //开关

    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @ExperimentalAnimationApi
    @Composable
    fun Editor(
        key: Preferences.Key<String>,
        title: String,
        icon: Painter? = null,
        label: String? = null,
        iconSpaceReserve: Boolean = true,
        itemClick: () -> Unit = {}
    ) {
        var visiable by remember {
            mutableStateOf(false)
        }
        var text by remember {
            mutableStateOf("")
        }
        context.launch {
            text = readData(key)
        }
        Column {
            BasicSetting(
                icon = icon,
                title = title,
                label = label,
                iconSpaceReserve = iconSpaceReserve,
                itemClick = {
                    visiable = !visiable
                    itemClick()
                }) {}

            AnimatedVisibility(visible = visiable) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .background(MaterialTheme.colors.settingBg),
                    value = text,
                    singleLine = true,
                    onValueChange = {
                        text = it
                        writeData(key, it)
                    })
            }
        }
    }


    @SuppressLint("CoroutineCreationDuringComposition")
    @ExperimentalAnimationApi
    @Composable
    fun Selector(
        key: Preferences.Key<Int>,//DataStore key
        title: String,//标题
        data: List<String>,
        icon: Painter? = null,//图标
        iconSpaceReserve: Boolean = true,
        label: String? = null,
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
            item = readData(key)
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

    @Composable
    private fun BasicSetting(
        icon: Painter?,
        title: String,
        label: String?,
        itemClick: (() -> Unit) = {},
        iconSpaceReserve: Boolean = true,
        content: @Composable () -> Unit = {}
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
                        painter = it,
                        contentDescription = title,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(start = 10.dp),
                        tint = MaterialTheme.colors.secondary
                    )
                }
                //文字
                Column(
                    modifier = Modifier.padding(start = if (iconSpaceReserve) 55.dp else 12.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
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

    private fun writeData(key: Preferences.Key<Boolean>, switch: Boolean) {
        context.launch {
            context.dataStore.edit {
                it[key] = switch
            }
        }
    }

    private fun writeData(key: Preferences.Key<String>, text: String) {
        context.launch {
            context.dataStore.edit {
                it[key] = text
            }
        }
    }

    private suspend fun readData(key: Preferences.Key<Int>) =
        context.dataStore.data.first()[key] ?: 0

    @JvmName("readData1")
    private suspend fun readData(key: Preferences.Key<Boolean>) =
        context.dataStore.data.first()[key] ?: false

    @JvmName("readData2")
    private suspend fun readData(key: Preferences.Key<String>) =
        context.dataStore.data.first()[key] ?: ""
}
