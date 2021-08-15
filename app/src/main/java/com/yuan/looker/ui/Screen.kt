package com.yuan.looker.ui

sealed class Screen(val route:String){
    object MainScreen:Screen("mainScreen")
    object SecondScreen:Screen("secondScreen")
    object TestScreen:Screen("testScreen")
}
