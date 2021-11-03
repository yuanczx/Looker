package com.yuan.looker.ui.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuan.looker.ui.theme.Gray300
import com.yuan.looker.ui.theme.Gray500

@ExperimentalAnimationApi
@Composable
fun BasicSetting(
    icon: Painter?,//图标
    title: String,//标题
    label: String?,//标签
    itemClick: (() -> Unit) = {},//点击事件
    iconSpaceReserve: Boolean = true,//保留图标占位
    expand: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit = {}//内容
) {
    var visible by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clickable {
                    itemClick()
                    visible = !visible
                },
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            //图标
            icon?.let {
                Icon(
                    painter = it,
                    contentDescription = title,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(start = 10.dp),
                    tint = MaterialTheme.colors.primary
                )
            }
            //文字
            Column(
                modifier = Modifier
                    .padding(start = if (iconSpaceReserve) 15.dp else 12.dp),
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
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                content()
            }

        }

    }
    if (expand != null) {
        AnimatedVisibility(visible = visible) {
            expand()
        }
    }
}