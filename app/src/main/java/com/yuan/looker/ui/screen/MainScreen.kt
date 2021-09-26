package com.yuan.looker.ui.screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.yuan.looker.R
import com.yuan.looker.activity.MainActivity
import com.yuan.looker.activity.splash
import com.yuan.looker.composable.NewsList
import com.yuan.looker.ui.Screen
import com.yuan.looker.ui.Tabs
import com.yuan.looker.ui.theme.Blue500
import com.yuan.looker.ui.theme.Blue700
import com.yuan.looker.ui.theme.Orange500
import com.yuan.looker.ui.theme.statusBar
import com.yuan.looker.viewmodel.NewsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainScreen(private val context: MainActivity) {
    //获取ViewModel
    private val viewModel by context.viewModels<NewsViewModel>()

    @Composable
    private fun MyTopBar(scaffoldState: ScaffoldState) {
        val scope = rememberCoroutineScope()
        TopAppBar(elevation = 0.dp,modifier = Modifier.background(MaterialTheme.colors.statusBar)) {
            IconButton(onClick = {
                scope.launch {
                    //开启（关闭）Drawer
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
                    tabNavController.navigate(Tabs.HomeTab.route) {
                        launchSingleTop = true
                    }
                    if (!viewModel.load) {
                        context.launch {
                            viewModel.load = true
                            delay(1000)
                            viewModel.load = false
                        }
                    }
                }

            }, icon = {
                Icon(imageVector = Icons.Outlined.Home, contentDescription = "Home")
            }, label = { Text(text = "Home") })
            BottomNavigationItem(selected = selectedTab == 2, onClick = {
                if (selectedTab != 2) {
                    selectedTab = 2
                    tabNavController.backQueue.removeLast()
                    tabNavController.navigate(Tabs.ShopTab.route) {
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
                    tabNavController.navigate(Tabs.UserTab.route)
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

    @ExperimentalCoilApi
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
                    startDestination = Tabs.HomeTab.route
                ) {
                    composable(Tabs.HomeTab.route) { HomeTab() }
                    composable(Tabs.ShopTab.route) { ShopTab() }
                    composable(Tabs.UserTab.route) { UserTab() }
                }
            }
        }
    }

    @ExperimentalCoilApi
    @SuppressLint("CoroutineCreationDuringComposition")
    @ExperimentalFoundationApi
    @Composable
    fun HomeTab() {
        //初始化变量
        val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
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
                    context.launch {
                        viewModel.newsIndex = 0
                        viewModel.loadNews(0)
                        swipeRefreshState.isRefreshing = false
                        Toast.makeText(
                            context,
                            context.resources.getString(R.string.refresh_success),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }) {
                var selectedTab by remember {
                    mutableStateOf(0)
                }
                Column(Modifier.fillMaxSize()) {
                    TabRow(selectedTabIndex = selectedTab,backgroundColor = MaterialTheme.colors.statusBar) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = {
                                if (selectedTab != 0) {
                                    selectedTab = 0
                                    viewModel.newsIndex = 0
                                    context.launch { viewModel.loadNews(0) }
                                }
                            },
                            text = { Text(text = "头条") })
                        Tab(
                            selected = selectedTab == 1,
                            onClick = {
                                if (selectedTab != 1) {
                                    selectedTab = 1
                                    viewModel.newsIndex = 0
                                    context.launch { viewModel.loadNews(1) }

                                }
                            },
                            text = { Text(text = "精选") })
                        Tab(
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            text = { Text(text = "科技") })
                    }
                    var isLoading by remember { mutableStateOf(false) }
                    viewModel.news?.let { newsList ->
                        NewsList(
                            newscast = newsList,
                            lastEvent = if (viewModel.load) null else { isLast ->
                                context.launch {
                                    if (viewModel.newsIndex < 440) {
                                        if (isLoading) return@launch
                                        isLoading = true
                                        if (isLast) viewModel.loadNews(selectedTab)
                                        isLoading = false
                                    }
                                }
                            })
                        {
                            context.navController.navigate(Screen.ReadScreen.route)
                        }
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

    @Composable
    fun UserTab() {
        Text(text = "User Tab")
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

