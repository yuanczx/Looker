package com.yuan.looker.ui.screen

import android.annotation.SuppressLint
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Settings
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
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.yuan.looker.R
import com.yuan.looker.activity.MainActivity
import com.yuan.looker.activity.splash
import com.yuan.looker.ui.composable.NewsList
import com.yuan.looker.ui.theme.Blue500
import com.yuan.looker.ui.theme.statusBar
import com.yuan.looker.utils.sealed.Screen
import com.yuan.looker.viewmodel.NewsViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
fun MainScreen(context: MainActivity) {
    //获取ViewModel
    val viewModel by context.viewModels<NewsViewModel>()
    val state = rememberScaffoldState()
    @Composable
    fun MyTopBar(scaffoldState: ScaffoldState) {
        val scope = rememberCoroutineScope()
        TopAppBar(backgroundColor = MaterialTheme.colors.statusBar, elevation = 0.dp) {
            IconButton(onClick = {
                scope.launch {
                    //开启（关闭）Drawer
                    if (scaffoldState.drawerState.isOpen)
                        scaffoldState.drawerState.close()
                    else scaffoldState.drawerState.open()
                }
            }, content = {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = "Menu",
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                )
            })
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 20.sp,
                fontWeight = FontWeight.W500
            )
        }
    }

    @Composable
    fun MyDrawer() {
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
                //退出应用
                IconButton(onClick = { context.finish() }) {
                    Icon(
                        imageVector = Icons.Rounded.ExitToApp,
                        contentDescription = "Exit",
                        tint = MaterialTheme.colors.primary
                    )
                }
                //收藏
                IconButton(onClick = { viewModel.message(R.string.construction) }) {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = MaterialTheme.colors.primary
                    )
                }
                //进入设置界面
                IconButton(onClick = { context.navController.navigate(Screen.SettingScreen.route) }) {
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
    @SuppressLint("CoroutineCreationDuringComposition")
    @ExperimentalFoundationApi
    @Composable
    fun Content() {
        //初始化变量
        val lazyListState = rememberLazyListState()
        val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)

        fun reloadNews() {
            context.launch {
                if (viewModel.load) {
                    viewModel.message(R.string.loading)
                    return@launch
                }
                viewModel.newsIndex = 0
                viewModel.loadNews(viewModel.selectedTab)
                swipeRefreshState.isRefreshing = false
            }
        }

        @Composable
        fun LookerTab(label: String, self: Int) {
            var clickTimes by remember { mutableStateOf(0) }
            Tab(
                selected = (viewModel.selectedTab == self),
                onClick = {
                    //双击跳转到列表顶部
                    context.launch {
                        clickTimes += 1
                        delay(500)
                        if (clickTimes != 0) {
                            clickTimes = 0
                        }
                    }
                    if (clickTimes >= 1) {
                        clickTimes = 0
                        context.launch {
                            lazyListState.scrollToItem(0)
                        }
                    }
                    /**********************************************/
                    if (viewModel.selectedTab != self) {
                        viewModel.selectedTab = self
                        viewModel.newsIndex = 0
                        viewModel.load = false
                        context.launch {
                            viewModel.loadNews(self)
                            lazyListState.scrollToItem(1)
                            cancel()
                        }
                    }
                },
                text = { Text(text = label) })
        }

        Column(Modifier.fillMaxSize(), horizontalAlignment = CenterHorizontally) {
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
                    reloadNews()
                }) {
                Column(Modifier.fillMaxSize()) {
                    TabRow(
                        selectedTabIndex = viewModel.selectedTab,
                        backgroundColor = MaterialTheme.colors.statusBar
                    ) {
                        val labels = viewModel.arrayRes(R.array.sorts)
                        repeat(5) { LookerTab(label = labels[it], self = it) }
                    }
                    NewsList(
                        newsList = viewModel.news,
                        listState = lazyListState,
                        lastEvent = { isLast ->
                            context.launch {
                                if (!viewModel.load) {
                                    if (isLast) viewModel.loadNews(viewModel.selectedTab)
                                }
                            }
                        },
                        itemClick = { newsID ->
                            viewModel.contentLoad = false
                            context.launch {
                                viewModel.newsID = newsID

                                viewModel.loadContent()
                            }
                            context.navController.navigate(Screen.ReadScreen.route)
                        })
                }
            }
        }
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

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            topBar = { MyTopBar(state) },
            drawerContent = { MyDrawer() },
            scaffoldState = state,
            content = { Content() }
        )
    }
}


