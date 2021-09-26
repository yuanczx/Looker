package com.yuan.looker.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.yuan.looker.instance.MyRetrofit
import com.yuan.looker.model.Content
import com.yuan.looker.ui.theme.*
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
    var news: List<Content>? by mutableStateOf(null)

    //新闻索引
    var newsIndex = 0

    //是否正在加载
    var load = false

    fun loadTheme(index: Int = themeIndex) = when (index) {
        0 -> BlueTheme
        1 -> OrangeTheme
        2 -> GreenTheme
        3 -> PurpleTheme
        else -> DarkColorPalette
    }

    fun isNewsEnd() = newsIndex >= 440


    suspend fun loadNews(tab: Int) {
        try {
            val response = when (tab) {
                0 -> MyRetrofit.api.getHeadline(newsIndex).awaitResponse()
                1 -> MyRetrofit.api.getSelection(newsIndex).awaitResponse()
                else -> MyRetrofit.api.getHeadline(newsIndex).awaitResponse()
            }
            //MyRetrofit.api.getHeadline(newsIndex).awaitResponse()
            if (response.isSuccessful) {
                val data = when (tab) {
                    0 -> response.body()!!.Headline!!
                    1 -> response.body()!!.Selection!!
                    else -> response.body()!!.Headline!!
                }
                data.removeIf { content ->
                    content.title.isEmpty() ||
                    content.imgsrc.isBlank() ||
                    if (news == null) false else news!!.contains(content)||
                    content.docid.isEmpty()
                }
                //response.body()!!.Headline!!
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