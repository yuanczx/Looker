package com.yuan.looker.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.yuan.looker.instance.MyRetrofit
import com.yuan.looker.model.Content
import com.yuan.looker.ui.theme.BlueTheme
import retrofit2.awaitResponse
import java.net.UnknownHostException

class NewsViewModel : ViewModel() {

    var lookerTheme by mutableStateOf(BlueTheme)
    var news: List<Content>? by mutableStateOf(null)
    var newsIndex = 0
    var load = false

    suspend fun loadNews(tab:Int) {
        try {
        val response = when(tab){
            0->MyRetrofit.api.getHeadline(newsIndex).awaitResponse()
            1->MyRetrofit.api.getSelection(newsIndex).awaitResponse()
            else->MyRetrofit.api.getHeadline(newsIndex).awaitResponse()
        }
            //MyRetrofit.api.getHeadline(newsIndex).awaitResponse()
            if (response.isSuccessful) {
                val data =when(tab){
                    0->response.body()!!.Headline!!
                    1->response.body()!!.Selection!!
                    else->response.body()!!.Headline!!
                }
                //response.body()!!.Headline!!
                news = if (newsIndex == 0) data else news?.plus(data)
                newsIndex += 10
                Log.d("index", newsIndex.toString())
            }}
        catch(e:UnknownHostException){
            Log.d("error","网络错误")
        }catch(e:Exception){
            Log.d("error",e.toString())
        }
    }

}