package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tt.invoicecreator.ui.components.texts.BodyLargeText

@Composable
fun BodyLargeTextWithCustomButton(
    buttonText:String,
    onClick: () -> Unit
)
{
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        BodyLargeText(
            text = buttonText
        )

        CustomIconButton(
            onClick = {
                onClick()
            },
            imageVector = Icons.Default.Add,
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
    }
}


