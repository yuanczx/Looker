package com.yuan.looker.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuan.looker.R
import kotlinx.coroutines.*


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
fun MainUi(content: @Composable () -> Unit) {
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

            } },
        bottomBar = { MyBottomBar() },
        drawerContent = { MyDrawer() },
        scaffoldState = state
    ) {
        content()
    }
}