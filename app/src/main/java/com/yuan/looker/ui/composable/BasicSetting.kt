package com.yuan.looker.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuan.looker.ui.theme.Gray300
import com.yuan.looker.ui.theme.Gray500

@Composable
fun BasicSetting(
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