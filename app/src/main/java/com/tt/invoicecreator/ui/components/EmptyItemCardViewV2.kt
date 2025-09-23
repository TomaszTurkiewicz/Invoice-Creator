package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun EmptyItemCardViewV2(
    position:Int,
    showPosition:Boolean,
    onClick: () -> Unit
) {
    CustomCardView (
        modifier = Modifier
            .clickable {
                onClick()
            }
    ){
        Text(
            text = if(showPosition)"Item: $position not chosen yet" else "Item not chosen yet",
            fontWeight = FontWeight.W700,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(5.dp)
        )
    }
}