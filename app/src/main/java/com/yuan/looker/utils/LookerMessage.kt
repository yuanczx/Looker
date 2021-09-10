package com.yuan.looker.utils

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yuan.looker.MainActivity
import com.yuan.looker.bean.News
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class LookerMessage(private val context: MainActivity) {

    fun getNews(url: String,onResponse:(List<News>)->Unit) {
        val request = Request.Builder()
            .url(url)
            .get()
            .build()
        context.client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("call:", "failure")
            }

            override fun onResponse(call: Call, response: Response) {
                var text = response.body!!.string()
                text?.let {
                    text = it.drop(18).dropLast(1)
                }
                val type = object : TypeToken<List<News>>() {}.type
                val gson = Gson()

                onResponse(gson.fromJson(text, type))
            }
        })

    }


}