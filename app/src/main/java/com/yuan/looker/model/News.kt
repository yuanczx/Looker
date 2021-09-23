package com.yuan.looker.model

import com.google.gson.annotations.SerializedName

data class News(
    @SerializedName("T1348647853363")
    val Headline: List<Content>?,
    @SerializedName("T1467284926140")
    val Selection: List<Content>?
)