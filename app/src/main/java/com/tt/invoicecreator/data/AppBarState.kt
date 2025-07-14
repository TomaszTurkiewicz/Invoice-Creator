package com.tt.invoicecreator.data

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable

data class AppBarState (
    val title: String = "",
    val action: (@Composable RowScope.() -> Unit)? = null
)