package com.yuan.looker.ui.compose

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import com.yuan.looker.ui.Tab
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yuan.looker.R
import com.yuan.looker.ui.Screen
import com.yuan.looker.ui.theme.*
import kotlinx.coroutines.*

@Composable
private fun MyTopBar(scaffoldState: ScaffoldState) {
    val scope = rememberCoroutineScope()
    TopAppBar {
        IconButton(onClick = {
            scope.launch {
                if (scaffoldState.drawerState.isOpen)
                    scaffoldState.drawerState.close()
                else scaffoldState.drawerState.open()
            }
        }) {
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
    BottomNavigation() {
        BottomNavigationItem(selected = true, onClick = { /*TODO*/ }, icon = {
            Icon(imageVector = Icons.Outlined.Home, contentDescription = "Home")
        }, label = { Text(text = "Home") })
        BottomNavigationItem(selected = false, onClick = { /*TODO*/ }, icon = {
            Icon(imageVector = Icons.Outlined.ShoppingCart, contentDescription = "Home")
        }, label = { Text(text = "Shopping") })
        BottomNavigationItem(selected = false, onClick = { /*TODO*/ }, icon = {
            Icon(imageVector = Icons.Outlined.AccountCircle, contentDescription = "Home")
        }, label = { Text(text = "Account") })
    }
}

@Composable
private fun MyDrawer() {
    Image(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(Green500),
        painter = painterResource(id = R.drawable.ic_muntain),
        contentDescription = "Code",
        contentScale = ContentScale.Crop
    )
    Text(text = "Drawer", fontSize = 16.sp)
}

@ExperimentalFoundationApi
@Composable
fun MainScreen(navController: NavController) {
    val state = rememberScaffoldState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { MyTopBar(state) },
        bottomBar = { MyBottomBar() },
        drawerContent = { MyDrawer() },
        scaffoldState = state
    ) {
        val tabNavController = rememberNavController()

        NavHost(navController = tabNavController,startDestination = Tab.HomeTab.route){
            composable("homeTab"){ HomeTab(navController = navController,tabNavController)}
            composable("userTab"){UserTab()}
        }
    }
}

@Composable
fun UserTab() {
    Text(text = "This is user tab")
}


@ExperimentalFoundationApi
@Composable
fun HomeTab(navController: NavController,tabNavController: NavController) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = CenterHorizontally) {
        Card(
            border = BorderStroke(4.dp, Color.Gray),
            elevation = 6.dp,
            modifier = Modifier
                .padding(10.dp)
                .size(120.dp)
                .clickable {
                    tabNavController.navigate(Tab.UserTab.route)
                }) {
            Text(text = "Hello world", modifier = Modifier.padding(10.dp))
        }
        Button(onClick = { navController.navigate(Screen.TestScreen.route) }) {
            Text(text = "TestScreen")

        }


        LazyVerticalGrid(
            cells = GridCells.Fixed(3),
            contentPadding = PaddingValues(vertical = 3.dp)
        ) {
            items(100) {
                Card(
                    Modifier
                        .size(120.dp)
                        .padding(10.dp)
                        .clickable { },
                    backgroundColor = Green200,
                    elevation = 5.dp,
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(text = "Hello world $it", color = Color.White)
                    }

                }
            }
        }
    }
}

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
fun TestScreen(dataStore: DataStore<androidx.datastore.preferences.core.Preferences>) {
    val scope = rememberCoroutineScope()
    val testKey = stringPreferencesKey("test")

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        var key by remember {
            mutableStateOf("")
        }
        var dataValue by remember { mutableStateOf("") }
        OutlinedTextField(
            value = key,
            onValueChange = { key = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )
        OutlinedTextField(
            value = dataValue,
            onValueChange = { dataValue = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )
        Button(modifier = Modifier.padding(10.dp),onClick = {
            scope.launch {
                dataStore.edit { settings ->
                    settings[testKey] = dataValue
                }
            }
        }) {
            Text(text = "Save Data")
        }
        Button(onClick = {
            scope.launch {
                dataStore.edit {
                    key = it[testKey].toString()
                }
            }
        }) {
            Text(text = "Get Data")
        }

    }
}
