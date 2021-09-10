package com.yuan.looker.ui.screen

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.yuan.looker.MainActivity
import com.yuan.looker.R
import com.yuan.looker.bean.News
import com.yuan.looker.composable.WebCompo
import com.yuan.looker.splash
import com.yuan.looker.ui.Screen
import com.yuan.looker.ui.Tab
import com.yuan.looker.ui.theme.Blue500
import com.yuan.looker.ui.theme.Blue700
import com.yuan.looker.ui.theme.Orange500
import com.yuan.looker.utils.LookerMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainScreen(private val context: MainActivity) {

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
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = CenterHorizontally
        ) {
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
                    .fillMaxWidth(0.9f)
                    .height(55.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Rounded.ExitToApp,
                        contentDescription = "Exit",
                        tint = MaterialTheme.colors.primary
                    )
                }

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = MaterialTheme.colors.primary
                    )
                }
                IconButton(onClick = {
                    context.navController.navigate(Screen.SettingScreen.route)
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "Settings",
                        tint = MaterialTheme.colors.primary
                    )
                }
            }

        }


    }

    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    @Composable
    fun Screen() {
        val tabNavController = rememberNavController()
        val state = rememberScaffoldState()
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = { MyTopBar(state) },
                bottomBar = { MyBottomBar(tabNavController) },
                drawerContent = { MyDrawer() },
                scaffoldState = state
            ) {
                NavHost(
                    navController = tabNavController,
                    startDestination = Tab.HomeTab.route
                ) {
                    composable(Tab.HomeTab.route) { HomeTab() }
                    composable(Tab.ShopTab.route) { ShopTab() }
                    composable(Tab.UserTab.route) { UserTab() }
                }
            }
        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @ExperimentalFoundationApi
    @Composable
    fun HomeTab() {
        //初始化变量
        var index = 0
        var itemNums by remember { mutableStateOf(0) }
        var newsList by remember { mutableStateOf(listOf(News("", ""))) }
        val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
        val lookerMessage = LookerMessage(context)
        fun loadNews() {
            lookerMessage.getNews("https://c.m.163.com/nc/article/headline/T1348647853363/${index}-10.html") {
                index += 10
                newsList = newsList + it
                itemNums = newsList.size
            }
            swipeRefreshState.isRefreshing = false
        }
        loadNews()
        //Text(modifier = Modifier.verticalScroll(rememberScrollState()).padding(bottom = 60.dp),text = html)
        Column(
            Modifier
                .fillMaxSize()
                .padding(bottom = 55.dp),
            horizontalAlignment = CenterHorizontally
        ) {
            SwipeRefresh(
                state = swipeRefreshState,
                indicator = { state, trigger ->
                    SwipeRefreshIndicator(
                        state,
                        trigger,
                        contentColor = MaterialTheme.colors.primaryVariant
                    )
                },
                onRefresh = {
                    swipeRefreshState.isRefreshing = true
                    loadNews()
                }) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(itemNums) {
                        Text(text = newsList[it].title)
                    }
                }
            }
        }

    }

    @Composable
    fun ShopTab() {
        Column(modifier = Modifier.fillMaxSize()) {
            val judge = remember { mutableStateOf(false) }
            val color by animateColorAsState(
                if (judge.value) Blue700 else Orange500, animationSpec = tween(
                    durationMillis = 2000
                )
            )
            Box(modifier = Modifier
                .background(color)
                .clickable { judge.value = !judge.value }
                .size(120.dp)) {}
        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    fun UserTab() {
        WebCompo(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            context = context,
            url = "https://yuanczx.github.io"
        )
    }


    @Composable
    fun Splash(splash: Boolean) {
        Column(
            modifier = if (splash) Modifier.size(0.dp) else Modifier
                .fillMaxSize()
                .background(Blue500),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally
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
}

