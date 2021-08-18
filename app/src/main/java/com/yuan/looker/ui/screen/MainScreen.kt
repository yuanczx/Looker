package com.yuan.looker.ui.screen

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yuan.looker.MainActivity
import com.yuan.looker.R
import com.yuan.looker.ui.Screen
import com.yuan.looker.ui.Tab
import com.yuan.looker.ui.theme.Green700
import kotlinx.coroutines.launch

class MainScreen(val context: MainActivity) {
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

            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 20.sp,
                fontWeight = FontWeight.W500
            )

        }
    }

    @Composable
    private fun MyBottomBar(tabNavController: NavController) {
        var selectedTab by remember {
            mutableStateOf(1)
        }
        BottomNavigation {
            BottomNavigationItem(selected = selectedTab == 1, onClick = {
                if (selectedTab != 1) {
                    selectedTab = 1
                    tabNavController.backQueue.removeLast()
                    tabNavController.navigate(Tab.HomeTab.route) {
                        launchSingleTop = true
                    }
                }

            }, icon = {
                Icon(imageVector = Icons.Outlined.Home, contentDescription = "Home")
            }, label = { Text(text = "Home") })
            BottomNavigationItem(selected = selectedTab == 2, onClick = {
                if (selectedTab != 2) {
                    selectedTab = 2
                    tabNavController.backQueue.removeLast()
                    tabNavController.navigate(Tab.ShopTab.route) {
                        launchSingleTop = true
                    }

                }
            }, icon = {
                Icon(imageVector = Icons.Outlined.ShoppingCart, contentDescription = "Home")
            }, label = { Text(text = "Shopping") })
            BottomNavigationItem(selected = selectedTab == 3, onClick = {
                if (selectedTab != 3) {
                    selectedTab = 3
                    tabNavController.backQueue.removeLast()
                    tabNavController.navigate(Tab.UserTab.route)
                }
            }, icon = {
                Icon(imageVector = Icons.Outlined.AccountCircle, contentDescription = "Home")
            }, label = { Text(text = "Account") })
        }
    }

    @Composable
    private fun MyDrawer() {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(MaterialTheme.colors.secondary),
                painter = painterResource(id = R.drawable.bg_ocean),
                contentDescription = "Code",
                contentScale = ContentScale.Crop
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Rounded.ExitToApp,
                        contentDescription = "Exit",
                        tint = Green700
                    )
                }

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = Green700
                    )
                }
                IconButton(onClick = {
                    context.navController.navigate(Screen.SettingScreen.route)
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "Settings",
                        tint = Green700
                    )
                }
            }

        }


    }

    @ExperimentalFoundationApi
    @Composable
    fun Screen() {
        val tabNavController = rememberNavController()
        val state = rememberScaffoldState()
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { MyTopBar(state) },
            bottomBar = { MyBottomBar(tabNavController) },
            drawerContent = { MyDrawer() },
            scaffoldState = state
        ) {
            NavHost(navController = tabNavController, startDestination = Tab.HomeTab.route) {
                composable("homeTab") { HomeTab() }
                composable("shopTab") { ShopTab() }
                composable(Tab.UserTab.route) { UserTab() }
            }
        }
    }

    @ExperimentalFoundationApi
    @Composable
    fun HomeTab() {
        var numbers by remember { mutableStateOf(100) }
        Column(
            Modifier
                .fillMaxSize()
                .padding(bottom = 55.dp), horizontalAlignment = CenterHorizontally
        ) {
            LazyVerticalGrid(
                cells = GridCells.Fixed(3),
                contentPadding = PaddingValues(vertical = 3.dp)
            ) {
                items(numbers) {
                    Card(
                        Modifier
                            .size(120.dp)
                            .padding(10.dp)
                            .clickable { numbers += 10 },
                        backgroundColor = MaterialTheme.colors.secondary,
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
    fun ShopTab() {
        Text(text = "This is shop tab")
    }

    @Composable
    fun UserTab() {
        Text(text = "This is user tab")
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
            horizontalAlignment = CenterHorizontally,

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
            Button(modifier = Modifier.padding(10.dp), onClick = {
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
}