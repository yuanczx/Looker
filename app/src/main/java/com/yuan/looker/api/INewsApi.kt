package com.yuan.looker.api

import com.yuan.looker.model.NetEaseNews
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface INewsApi {
//    @GET("T1348647853363/{index}-10.html")
//    fun getHeadline(@Path("index")index:Int):Call<News>
//
//    @GET("T1467284926140/{index}-10.html")
//    fun getSelection(@Path("index")index:Int):Call<News>

    @GET("{sort}/{index}-10.html")
    fun getNews(@Path("sort")sort:String, @Path("index")index:Int):Call<NetEaseNews>
}