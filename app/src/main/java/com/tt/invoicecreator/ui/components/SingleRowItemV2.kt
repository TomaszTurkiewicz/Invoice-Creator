package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
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
import com.tt.invoicecreator.data.roomV2.entities.ItemV2

@Composable
fun SingleRowItemV2(
    item: ItemV2,
    itemChosen: (ItemV2) -> Unit
) {
    CustomCardView(
        modifier = Modifier
            .clickable {
                itemChosen(item)
            }
    ) {
        Row(
            modifier = Modifier
        )
        {
            Row {
                Text(
                    text = item.itemName,
                    fontWeight = FontWeight.W700,
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .padding(5.dp)
                )
                Text(
                    text = item.itemValue.toString(),
                    fontWeight = FontWeight.W700,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                )
            }
        }
    }
}