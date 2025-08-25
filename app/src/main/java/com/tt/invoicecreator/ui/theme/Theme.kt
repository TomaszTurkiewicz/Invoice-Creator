package com.tt.invoicecreator.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkMyColorScheme = CustomColorsPalette(
    material = darkColorScheme(
        primary = Purple40,
        primaryContainer = Pink40,
        background = PurpleGrey40
    ),
    invoicePrimary = Orange,
    primaryDark = Grey
)

private val LightMyColorScheme = CustomColorsPalette(
    material = lightColorScheme(
        primary = Purple80,
        primaryContainer = Pink80,
        background = PurpleGrey80
    ),
    invoicePrimary = Orange,
    primaryDark = Grey
)

val LocalColors = staticCompositionLocalOf { LightMyColorScheme }
@Composable
fun InvoiceCreatorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    content: @Composable () -> Unit
) {
    val systemsUiController = rememberSystemUiController()
    val colors = when{
        darkTheme -> {
            DarkMyColorScheme
        }
        else -> {
            LightMyColorScheme
        }
    }
    CompositionLocalProvider {
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