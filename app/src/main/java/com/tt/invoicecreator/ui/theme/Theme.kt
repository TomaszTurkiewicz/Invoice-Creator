package com.tt.invoicecreator.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkMyColorScheme = CustomColorsPalette(
    material = darkColorScheme(
        primary = Blue500Dark,
        primaryContainer = Blue100Dark,
        background = Blue50Dark
    ),
    invoicePrimary = Orange,
    primaryDark = Blue700Dark,
    primaryLight = Blue200Dark,
    error = Red700Dark,
    success = Green700Dark
)

private val LightMyColorScheme = CustomColorsPalette(
    material = lightColorScheme(
        primary = Blue500,
        primaryContainer = Blue100,
        background = Blue50
    ),
    invoicePrimary = Orange,
    primaryDark = Blue900,
    primaryLight = Blue200,
    error = Red700,
    success = Green700
)

val LocalColors = staticCompositionLocalOf { LightMyColorScheme }
@Composable
fun InvoiceCreatorTheme(
    darkTheme: Boolean = false,
    // Dynamic color is available on Android 12+
    content: @Composable () -> Unit
) {
    val systemsUiController = rememberSystemUiController()
    // 3. This will now always pick LightMyColorScheme because darkTheme is false
    val colors = if (darkTheme) DarkMyColorScheme else LightMyColorScheme

    // 4. Update the Status Bar color to always be the Light version
    SideEffect {
        systemsUiController.setStatusBarColor(
            color = colors.primaryDark,
            darkIcons = true
        )
    }
    CompositionLocalProvider(LocalColors provides colors) {
        MaterialTheme(
            colorScheme = colors.material,
            typography = Typography,
            content = content
        )
    }

    if(darkTheme){
        systemsUiController.setStatusBarColor(
            DarkMyColorScheme.primaryDark
        )
    }else{
        systemsUiController.setStatusBarColor(
            LightMyColorScheme.primaryDark
        )
    }


}