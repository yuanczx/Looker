package com.yuan.looker.ui

sealed class Screen(val route:String){
    object MainScreen:Screen("mainScreen")
    object SecondScreen:Screen("secondScreen")
    object TestScreen:Screen("testScreen")
}

sealed class Tab(val route:String){
    object HomeTab:Tab("homeTab")
    object UserTab:Tab("userTab")
    object ShopTab:Tab("shopTab")
}
