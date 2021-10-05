package com.yuan.looker.utils.sealed

    sealed class Screen(val route:String){
        object MainScreen: Screen("mainScreen")
        object ReadScreen: Screen("readScreen")
        object TestScreen: Screen("testScreen")
        object SettingScreen: Screen("settingScreen")
}
