package com.yuan.looker.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yuan.looker.R
import com.yuan.looker.model.NetEaseNewsItem
import com.yuan.looker.ui.theme.*
import com.yuan.looker.utils.MyRetrofit
import com.yuan.looker.utils.sealed.Sort
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import java.net.UnknownHostException

class NewsViewModel(application: Application) : AndroidViewModel(application) {
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


    private fun context() = getApplication<Application>().applicationContext
    private var toast: Toast = Toast(context())
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
    var load by mutableStateOf(false)

    //当前新闻
    var currentNews by mutableStateOf("")

    var selectedTab by mutableStateOf(0)

    var contentLoad by mutableStateOf(false)

    fun loadTheme(index: Int = themeIndex) = when (index) {
        0 -> BlueTheme
        1 -> OrangeTheme
        2 -> GreenTheme
        3 -> PurpleTheme
        else -> DarkColorPalette
    }

    private fun isNewsEnd() = newsIndex >= 440


    private fun getSort(index: Int) = when (index) {
        0 -> Sort.News
        1 -> Sort.Financial
        2 -> Sort.Tech
        3 -> Sort.Army
        4 -> Sort.Digital
        else -> Sort.News
    }


    fun arrayRes(id: Int): Array<String> = context().resources.getStringArray(id)

    // private fun stringRes(id: Int) = context().getString(id)

    private var toastShowing = false
    fun message(msgID: Int) {
        if (toastShowing) return
        toast.setText(msgID)
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
        toastShowing = true
        viewModelScope.launch {
            delay(1500)
            toast.cancel()
            toastShowing = false
        }
    }

    //加载新闻
    suspend fun loadNews(tab: Int) {
        if (isNewsEnd()) {
            message(R.string.no_more)
            return
        }
        if (load) return else load = true
        val sort = getSort(tab)
        try {
            val response = MyRetrofit.api.getNews(sort.name, newsIndex).awaitResponse()
            if (response.isSuccessful) {
                val data = response.body() ?: return
                //过滤：移除空元素
                data.removeIf {
                    with(it) {
                        docid.isBlank() || title.isBlank() || imgsrc.isBlank() || source.isBlank()
                    }
                }
                if (newsIndex == 0) {
                    news = data
                    message(R.string.refresh_success)
                } else {
                    news = news.plus(data).toMutableList()
                }
                newsIndex += 10
                Log.d("index", newsIndex.toString())
                load = false
            }
        } catch (e: UnknownHostException) {
            load = false
            if (newsIndex == 0)
                message(R.string.refresh_fail)
            else
                message(R.string.load_fail)
            load = false
        } catch (e: Exception) {
            load = false
            if (newsIndex == 0)
                message(R.string.refresh_fail)
            else
                message(R.string.load_fail)
            load = false
        }
    }


    //加载新闻内容
    suspend fun loadContent() {
        if (contentLoad) return else contentLoad = true
        val dark = lookerTheme == DarkColorPalette
        try {
            val response = MyRetrofit.api.getHtml(newsID).awaitResponse()
            if (response.isSuccessful) {
                currentNews = with(response.body() ?: return) {
                    val s = getCss(dark) + substring(
                        indexOf("<div class=\"article-content\">"),
                        indexOf("</article>")
                    )
                        .replace("//nimg", "https://nimg")
                        .replace("data-src=", "src=")
                        .replace("href=\"//", "href=\"https://")
                        .replace("data-href=", "href=")
                        .replace("alt=\"\"", "")
                        .replace("href=\"#\"", "")
                        .replace("http://", "https://")
                        .replace("<span>&#xe61d;<span>", "")
                        .replace(
                            "<!--yinsurance=endAD_insurance-->",
                            "<div class=\"insurance\">"
                        ) + "</div></body></html>"
                    s
                }
                contentLoad = false
            }
        } catch (e: Exception) {
            contentLoad = false
            Log.d("error", e.message ?: return)
        }
    }

}