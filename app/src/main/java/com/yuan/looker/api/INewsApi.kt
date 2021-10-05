package com.yuan.looker.api

import com.yuan.looker.model.NetEaseNews
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface INewsApi {
    @GET("{sort}/{index}-10.html")
    fun getNews(@Path("sort")sort:String, @Path("index")index:Int):Call<NetEaseNews>
}