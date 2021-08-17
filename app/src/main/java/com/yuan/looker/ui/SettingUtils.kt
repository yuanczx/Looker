package com.yuan.looker.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.yuan.looker.ui.theme.Gray500
import com.yuan.looker.ui.theme.Gray700
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SettingUtils(
    private val dataStore: DataStore<Preferences>,
    private val scope: CoroutineScope
) {

    @SuppressLint("CoroutineCreationDuringComposition", "UnrememberedMutableState")
    @Composable
    fun SettingSwitch(
        key: Preferences.Key<Boolean>,
        title: String = "",
        icon: ImageVector? = null,
        label: String? = null,
    ) {

        var switch by remember {
            mutableStateOf(false)
        }
        scope.launch {
            dataStore.data.collect {
                switch =it[key]?:false
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clickable {
                    setData(key, !switch)
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicSetting(icon, title, label)
            Switch(checked = switch, modifier = Modifier.padding(end = 5.dp), onCheckedChange = {
                switch = it
                setData(key, it)
            })
        }

    }

    @Composable
    private fun BasicSetting(
        icon: ImageVector?,
        title: String,
        label: String?
    ) {
        Box(contentAlignment = Alignment.CenterStart) {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = title,
                    modifier = Modifier
                        .size(42.dp)
                        .padding(start = 10.dp)
                )
            }
            Column(
                modifier = Modifier.padding(start = 55.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "This is a Test",
                    fontSize = 16.sp,
                    color = Gray700,
                    fontWeight = FontWeight.W500
                )
                label?.let {
                    Text(text = it, fontSize = 14.sp, color = Gray500)
                }
            }
        }
    }

    @Composable
    fun SettingMulti(
        key:Preferences.Key<Int>,
        title: String,
        label:String?=null,
        icon: ImageVector?=null
    ){
        BasicSetting(icon = icon, title = title, label = label)

    }





private fun setData(key: Preferences.Key<Boolean>, value: Boolean) =
    scope.launch {
        dataStore.edit { it[key] = value }
    }

private fun setData(key: Preferences.Key<String>, value: String) =
    scope.launch {
        dataStore.edit { it[key] = value }
    }


}