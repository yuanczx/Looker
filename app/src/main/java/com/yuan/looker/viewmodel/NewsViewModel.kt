package com.yuan.looker.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.yuan.looker.model.NetEaseNewsItem
import com.yuan.looker.ui.theme.*
import com.yuan.looker.utils.MyRetrofit
import com.yuan.looker.utils.sealed.Sort
import retrofit2.awaitResponse
import java.net.UnknownHostException

class NewsViewModel : ViewModel() {
    //主题索引
    var themeIndex = 0

    //深色模式
    var darkMode by mutableStateOf(false)

    //主题
    var lookerTheme by mutableStateOf(BlueTheme)

    //新闻列表
    var news: List<NetEaseNewsItem>? by mutableStateOf(null)

    //新闻索引
    var newsIndex = 0

    //是否正在加载
    var load = false

    //当前新闻
    var currentUrl = ""

    fun loadTheme(index: Int = themeIndex) = when (index) {
        0 -> BlueTheme
        1 -> OrangeTheme
        2 -> GreenTheme
        3 -> PurpleTheme
        else -> DarkColorPalette
    }

    fun isNewsEnd() = newsIndex >= 440


    private fun getSort(index: Int)=when(index){
        0->Sort.News
        1->Sort.Financial
        2->Sort.Tech
        else -> Sort.News
    }

    //加载新闻
    suspend fun loadNews(tab: Int) {
        val sort = getSort(tab)
        try {
            val response = MyRetrofit.api.getNews(sort.name,newsIndex).awaitResponse()
            if (response.isSuccessful) {
                val data = response.body()!!
                //过滤：移除空元素
                data.removeIf {
                    with(it){
                        url.isBlank()||title.isBlank()||imgsrc.isBlank()
                    }
                }
                news = if (newsIndex == 0) data else news?.plus(data)
                newsIndex += 10
                Log.d("index", newsIndex.toString())
            }
        } catch (e: UnknownHostException) {
            Log.d("error", "网络错误")
        } catch (e: Exception) {
            Log.d("error", e.toString())
        }
    }
}