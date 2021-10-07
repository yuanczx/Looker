package com.yuan.looker.ui.screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainScreen(private val context: MainActivity) {
    //获取ViewModel
    private val viewModel by context.viewModels<NewsViewModel>()

    @Composable
    private fun MyTopBar(scaffoldState: ScaffoldState) {
        val scope = rememberCoroutineScope()
        TopAppBar(backgroundColor = MaterialTheme.colors.statusBar, elevation = 0.dp) {
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
        val state = rememberScaffoldState()
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
                topBar = { MyTopBar(state) },
                drawerContent = { MyDrawer() },
                scaffoldState = state,
                content = { HomeTab() }
            )
        }
    }

    @ExperimentalCoilApi
    @SuppressLint("CoroutineCreationDuringComposition")
    @ExperimentalFoundationApi
    @Composable
    fun HomeTab() {
        //初始化变量
        var selectedTab by remember { mutableStateOf(0) }
        val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
        fun reloadNews(){
            context.launch {
                viewModel.newsIndex = 0
                viewModel.load = true
                viewModel.loadNews(selectedTab)
                viewModel.load = false
                swipeRefreshState.isRefreshing = false
                Toast.makeText(
                    context,
                    context.resources.getString(R.string.refresh_success),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        @Composable
        fun LookerTab(label:String,self:Int){
            Tab(
                selected = selectedTab == self,
                onClick = {
                    if (selectedTab != self) {
                        selectedTab = self
                        viewModel.newsIndex = 0
                        viewModel.load = false
                        context.launch { viewModel.loadNews(self) }
                    }
                },
                text = { Text(text = label) })
        }
        //Text(modifier = Modifier.verticalScroll(rememberScrollState()).padding(bottom = 60.dp),text = html)
        Column(
            Modifier.fillMaxSize(),
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
                    reloadNews()
                }) {
                Column(Modifier.fillMaxSize()) {
                    TabRow(
                        selectedTabIndex = selectedTab,
                        backgroundColor = MaterialTheme.colors.statusBar
                    ) {
                        val labels = listOf("新闻","财经","科技")
                        repeat(3){
                            LookerTab(label = labels[it] , self = it)
                        }
                    }
                    viewModel.news?.let { newsList ->
                        NewsList(
                            newscast = newsList,
                            lastEvent = { isLast ->
                                context.launch {
                                    if (!(viewModel.isNewsEnd() || viewModel.load)) {
                                        if (viewModel.load || viewModel.isNewsEnd()) return@launch
                                        viewModel.load = true
                                        if (isLast) viewModel.loadNews(selectedTab)
                                        delay(100)
                                        viewModel.load = false
                                    }
                                }
                            })
                        { docid->
                            context.launch {
                                viewModel.loadContent(docid)
                            }
                            context.navController.navigate(Screen.ReadScreen.route)

                        }
                    }

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
}

