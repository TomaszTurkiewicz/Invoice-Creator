package com.tt.invoicecreator.ui.components.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.ui.components.texts.TitleLargeText

@Composable
fun EmptyItemCardViewV2(
    position:Int,
    showPosition:Boolean,
    onClick: () -> Unit
) {
    CustomCardView(
        modifier = Modifier
            .clickable {
                onClick()
            }
    ) {
        TitleLargeText(
            text = if (showPosition) "Item: $position not chosen yet" else "Item not chosen yet",
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )
    }
}