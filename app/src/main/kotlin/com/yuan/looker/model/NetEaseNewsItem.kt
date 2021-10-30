package com.yuan.looker.model

data class NetEaseNewsItem(
    val commentCount: Int,
    val digest: String,
    val docid: String,
    val hasImg: Int,
    val imgsrc: String,
    val imgsrc3gtype: String,
    val liveInfo: Any,
    val priority: Int,
    val ptime: String,
    val source: String,
    val stitle: String,
    val title: String,
    val url: String
)