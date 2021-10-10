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
    companion object {
        private fun getCss(dark: Boolean) = """<html><head><style>  
         body{background-color:${if (dark) "Black" else "White"}}
         a{color:DodgerBlue;text-decoration:none}
         .title{color:${if (dark) "WhiteSmoke" else "Black"};font-size:25px}
         .time{color:gray;}
         blockquote{border-left: 5px solid Gainsboro;background-color:GhostWhite;padding-left:10px}
         p{text-indent:0;line-height:30px;letter-spacing:1.5px;font-size:20px;color:${if (dark) "WhiteSmoke" else "DimGray"}}
         img{width:100%;margin-top:10px}
         video{display: block;max-width:100%;margin-top:10px;margin-bottom:30px;width:100%}
         .insurance p{text-indent:0px;font-size:15px;color:gray;line-height:18px}
         .editor{text-indent:0px;font-size:13px;color:gray}
         .bot_word{display: none;}
         </style></head> <body>"""

    }

    var newsID by mutableStateOf("")

    //主题索引
    var themeIndex = 0

    //深色模式
    var darkMode by mutableStateOf(false)

    //主题
    var lookerTheme by mutableStateOf(BlueTheme)

    //新闻列表
    var news: List<NetEaseNewsItem> by mutableStateOf(listOf())

    //新闻索引
    var newsIndex = 0

    //是否正在加载
    var load = false

    //当前新闻
    var currentNews by mutableStateOf("")

    var selectedTab by mutableStateOf(0)

    fun loadTheme(index: Int = themeIndex) = when (index) {
        0 -> BlueTheme
        1 -> OrangeTheme
        2 -> GreenTheme
        3 -> PurpleTheme
        else -> DarkColorPalette
    }

    fun isNewsEnd() = newsIndex >= 440


    private fun getSort(index: Int) = when (index) {
        0 -> Sort.News
        1 -> Sort.Financial
        2 -> Sort.Tech
        3 -> Sort.Army
        else -> Sort.News
    }

    //加载新闻
    suspend fun loadNews(tab: Int) {
        val sort = getSort(tab)
        try {
            val response = MyRetrofit.api.getNews(sort.name, newsIndex).awaitResponse()
            if (response.isSuccessful) {
                val data = response.body()!!
                //过滤：移除空元素
                data.removeIf {
                    with(it) {
                        docid.isBlank() || title.isBlank() || imgsrc.isBlank() || source.isBlank()
                    }
                }
                if (newsIndex==0) news = listOf()
                news = news.plus(data)
                newsIndex += 10
                Log.d("index", newsIndex.toString())
            }
        } catch (e: UnknownHostException) {
            Log.d("error", "网络错误")
        } catch (e: Exception) {
            Log.d("error", e.toString())
        }
    }


    suspend fun loadContent() {
        val dark = lookerTheme == DarkColorPalette
        try {
            val response = MyRetrofit.api.getHtml(newsID).awaitResponse()
            if (response.isSuccessful) {
                currentNews = with(response.body()!!) {
                    getCss(dark) + substring(
                        indexOf("<div class=\"article-content\">"),
                        indexOf("</article>")
                    )
                        .replace("//nimg", "https://nimg")
                        .replace("data-src=", "src=")
                        .replace("href=\"//", "href=\"https://")
                        .replace("http://", "https://")
                        .replace(
                            "<!--yinsurance=endAD_insurance-->",
                            "<div class=\"insurance\">"
                        ) + "</div></body></html>"
                }
                Log.d("html", currentNews)
            }
        } catch (e: Exception) {
            Log.d("error", e.message!!)
        }
    }

}