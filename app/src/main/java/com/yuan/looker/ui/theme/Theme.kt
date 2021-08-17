package com.yuan.looker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

val OrangeTheme = lightColors(
    primary = Orange500,
    primaryVariant = Orange700,
    secondary = Orange200
)
val DarkColorPalette = darkColors(
    primary = Green500,
    primaryVariant = Green700,
    secondary = Green200
)

 val LightColorPalette = lightColors(
    primary = Green500,
    primaryVariant = Green700,
    secondary = Green200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
inline fun  LookerTheme(theme:Colors= LightColorPalette, darkTheme: Boolean = isSystemInDarkTheme(), noinline content: @Composable() () -> Unit) {

    MaterialTheme(
        colors = if(darkTheme) DarkColorPalette else theme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}