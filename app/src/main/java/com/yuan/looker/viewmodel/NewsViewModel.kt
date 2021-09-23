package com.yuan.looker.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuan.looker.instance.MyRetrofit
import com.yuan.looker.model.Content
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class NewsViewModel: ViewModel() {
    var headline:List<Content>? by mutableStateOf(null)
    var selection:List<Content>? by mutableStateOf(null)
    var headlineIndex = 0

    fun loadHeadline(){
        viewModelScope.launch {
            val response = MyRetrofit.api.getHeadline(headlineIndex).awaitResponse()
            if (response.isSuccessful){
                val data = response.body()!!.Headline!!
                headline = if (headlineIndex==0)data else
                headline?.plus(data)
                headlineIndex+=10
            }
    }
}
}