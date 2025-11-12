package com.tt.invoicecreator.ui.components.texts

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.ui.theme.myColors

@Composable
fun BodyLargeTextWithFrame(
    modifier: Modifier = Modifier,
    text: String
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(1.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.myColors.primaryDark,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 8.dp, vertical = 6.dp), // Inner padding
        contentAlignment = Alignment.Center
    ) {
        BodyLargeText(text = text)
    }
}