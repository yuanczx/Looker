package com.yuan.looker.ui

sealed class Screen(val route:String){
    object MainScreen:Screen("mainScreen")
    object ReadScreen:Screen("readScreen")
    object TestScreen:Screen("testScreen")
    object SettingScreen:Screen("settingScreen")
}

sealed class Tabs(val route:String){
    object HomeTab:Tabs("homeTab")
    object UserTab:Tabs("userTab")
    object ShopTab:Tabs("shopTab")
}
