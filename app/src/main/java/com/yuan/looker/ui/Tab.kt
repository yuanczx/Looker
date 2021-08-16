package com.yuan.looker.ui

sealed class Tab(val route:String){
    object HomeTab:Tab("homeTab")
    object UserTab:Tab("userTab")
}
