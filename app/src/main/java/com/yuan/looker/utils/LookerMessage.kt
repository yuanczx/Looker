package com.yuan.looker.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yuan.looker.MainActivity
import com.yuan.looker.bean.News
import okhttp3.Request

class LookerMessage(private  val context: MainActivity) {

     fun getNews(url: String): List<News> {
        val request = Request.Builder()
            .url(url)
            .get()
            .build()
        var text = context.client.newCall(request).execute().body?.string()
        text?.let {
            text = it.drop(18).dropLast(1)
        }
        val type = object : TypeToken<List<News>>() {}.type
        val gson = Gson()
        gson.fromJson<List<News>>(text,type)
        return gson.fromJson(text, type)
    }


}