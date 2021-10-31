package com.yuan.looker.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuan.looker.activity.MainActivity
import com.yuan.looker.activity.splash
import com.yuan.looker.ui.theme.Blue700
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Splash(context: MainActivity, splash: Boolean, bg: Color = Blue700) {
    Column(
        modifier = if (splash) Modifier.size(0.dp) else Modifier
            .fillMaxSize()
            .background(bg),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Looker",
            fontSize = 50.sp,
            fontWeight = FontWeight.W900,
            textAlign = TextAlign.Center,
            color = Color.White
        )
        context.launch {
            delay(500)
            context.splash = true
        }
    }
}
