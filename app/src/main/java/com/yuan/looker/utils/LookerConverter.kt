package com.yuan.looker.utils

import android.util.Log
import com.google.gson.Gson
import com.yuan.looker.model.NetEaseNews
import okhttp3.ResponseBody
import retrofit2.Converter

class LookerConverter:Converter<ResponseBody,NetEaseNews> {
    override fun convert(value: ResponseBody): NetEaseNews? {
        val json = value.string().drop(29).dropLast(2)
        Log.d("Json",json)
        val gson = Gson()
        return gson.fromJson(json, NetEaseNews::class.java)
    }
}