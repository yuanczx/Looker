package com.yuan.looker.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yuan.looker.model.NetEaseNews
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class LookerConverterFactory:Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> ?{


        if (TypeToken.get(type).equals(TypeToken.get(String::class.java))) { return HtmlConverter() }
        if (TypeToken.get(type).equals(TypeToken.get(NetEaseNews::class.java))) { return LookerConverter() }
        return null
    }

    class LookerConverter:Converter<ResponseBody, NetEaseNews> {
        override fun convert(value: ResponseBody): NetEaseNews? {
            val json = value.string().drop(29).dropLast(2)
            val gson = Gson()
            return gson.fromJson(json, NetEaseNews::class.java)
        }
    }

    class HtmlConverter:Converter<ResponseBody,String>{
        override fun convert(value: ResponseBody): String? {
            return  value.string()
        }

    }
}