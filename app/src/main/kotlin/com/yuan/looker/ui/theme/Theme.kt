package com.yuan.looker.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


val BlueTheme = lightColors(
    primaryVariant = Blue700,
    primary = Blue500,
    secondary = Blue300,
)
val PurpleTheme = lightColors(
    primaryVariant = Purple700,
    primary = Purple500,
    secondary = Purple200,
)
val OrangeTheme = lightColors(
    primary = Orange500,
    primaryVariant = Orange700,
    secondary = Orange200,
)
val DarkColorPalette = darkColors(
    primary = Green500,
    primaryVariant = Green700,
    secondary = Green200,
)

val GreenTheme = lightColors(
    primary = Green500,
    primaryVariant = Green700,
    secondary = Green200
)

/* Other default colors to override
background = Color.White,
surface = Color.White,
onPrimary = Color.White,
onSecondary = Color.Black,
onBackground = Color.Black,
onSurface = Color.Black,
*/
val Colors.statusBar: Color @Composable get() = if (isLight) primary else Gray800

val Colors.settingBg: Color @Composable get() = if (isLight) Gray100 else Gray800

val Colors.listBack:  Color @Composable get() = if (isLight) Gray200 else Gray800
@Composable
fun LookerTheme(
    theme: Colors = BlueTheme,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors =  theme,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}