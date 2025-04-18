package lin.example.stock.presentation.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
val DarkGreen = Color(0xFF006400)

/** 自訂顏色集合 **/
data class CustomColors(
    val fall: Color
)

/** 全域 CompositionLocal **/
val LocalCustomColors = staticCompositionLocalOf {
    CustomColors(fall = DarkGreen)
}