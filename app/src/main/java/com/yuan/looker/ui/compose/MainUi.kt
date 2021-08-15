package com.yuan.looker.ui.compose

import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.viewpager2.widget.ViewPager2
import com.yuan.looker.R
import com.yuan.looker.ui.Screen
import kotlinx.coroutines.*
import kotlin.concurrent.thread


@Composable
private fun MyTopBar() {
    TopAppBar {
        IconButton(onClick = {}) {
            Icon(
                Icons.Default.Menu,
                contentDescription = "Menu",
                modifier = Modifier.padding(start = 10.dp, end = 10.dp)
            )
        }

        Text(text = stringResource(id = R.string.app_name), fontSize = 20.sp)

    }
}

@Composable
private fun MyBottomBar() {

}

@Composable
private fun MyDrawer() {

}

@Composable
fun MainScreen(navController: NavController) {

    val state = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar {
                IconButton(onClick = {
                    if (state.drawerState.isOpen)
                        scope.launch { state.drawerState.close() }
                    else scope.launch { state.drawerState.open() }
                }) {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = "Menu",
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                    )
                }

                Text(text = stringResource(id = R.string.app_name), fontSize = 20.sp)

            }
        },
        bottomBar = { MyBottomBar() },
        drawerContent = { MyDrawer() },
        scaffoldState = state
    ) {
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Card(
                border = BorderStroke(4.dp, Color.Gray),
                elevation = 6.dp,
                modifier = Modifier
                    .padding(10.dp)
                    .size(120.dp)
                    .clickable {
                        navController.navigate(Screen.SecondScreen.route)
                    }) {
                Text(text = "Hello world", modifier = Modifier.padding(10.dp))
            }
            Button(onClick = { navController.navigate(Screen.TestScreen.route) }) {
                Text(text = "TestScreen")

            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SecondScreen() {
    Column(modifier = Modifier.fillMaxSize()) {

        val isSt = remember {
            mutableStateOf(false)
        }
        val progress by animateFloatAsState(
            targetValue = if (isSt.value) 270f else 0f,
            animationSpec = tween(
                durationMillis = 1000,
                delayMillis = 0,
                easing = FastOutLinearInEasing
            )
        )
        Button(onClick = { isSt.value = !isSt.value }) {
            Text(text = "Click")

        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center)
        {
            Canvas(modifier = Modifier.size(120.dp)) {
                drawArc(
                    color = Color.Black,
                    startAngle = -90f,
                    sweepAngle = progress,
                    useCenter = false,
                    style = Stroke(width = 10f, cap = StrokeCap.Round)
                )
            }
        }
    }
}

@Composable
fun TestScreen() {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        var key by remember {
            mutableStateOf("")
        }
        var dataValue by remember { mutableStateOf("") }
        TextField(
            value = key,
            onValueChange = { key = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )
        TextField(
            value = dataValue,
            onValueChange = { dataValue = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )
        Button(onClick = { }) {
            Text(text = "save")
        }
        TextField(
            value = key,
            onValueChange = { key = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )
        TextField(
            value = dataValue,
            onValueChange = { dataValue = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )
        Button(onClick = { }) {
            Text(text = "save")
        }

    }
}