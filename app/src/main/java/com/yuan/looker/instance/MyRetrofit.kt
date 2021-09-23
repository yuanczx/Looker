package com.yuan.looker.instance

import com.yuan.looker.api.INewsApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//const val BASE_URL = "https://getman.cn/mock/"

object MyRetrofit {
    private const val BASE_URL = "https://c.3g.163.com/nc/article/list/"
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api:INewsApi by lazy {
        retrofit.create(INewsApi::class.java)
    }
}