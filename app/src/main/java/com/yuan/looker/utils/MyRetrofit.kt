package com.yuan.looker.utils

import com.yuan.looker.api.INewsApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MyRetrofit {
   //private const val BASE_URL = "https://c.3g.163.com/nc/article/list/"
    private const val BASE_URL = "https://3g.163.com/touch/reconstruct/article/list/"
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(LookerConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api:INewsApi by lazy {
        retrofit.create(INewsApi::class.java)
    }

}
