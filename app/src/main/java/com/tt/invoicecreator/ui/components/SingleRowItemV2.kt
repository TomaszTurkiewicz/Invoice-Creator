package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.roomV2.entities.ItemV2
import com.tt.invoicecreator.ui.components.cards.CustomCardView
import com.tt.invoicecreator.ui.components.texts.BodyLargeText

@Composable
fun SingleRowItemV2(
    item: ItemV2,
    itemChosen: (ItemV2) -> Unit
) {

    // Determine the text format based on the currency prefix
    val valueText = if (item.itemCurrency.prefix) {
        "${item.itemCurrency.symbol}${item.itemValue}"
    } else {
        "${item.itemValue}${item.itemCurrency.symbol}"
    }

    CustomCardView(
        modifier = Modifier
            .clickable {
                itemChosen(item)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween // Use SpaceBetween for alignment
        ) {
            BodyLargeText(
                text = item.itemName,
                modifier = Modifier
                    .weight(1f) // Use weight for flexible sizing
                    .padding(5.dp)
            )
            BodyLargeText(
                text = valueText, // Use the new formatted value text
                modifier = Modifier
                    .padding(5.dp),
                textAlign = TextAlign.End // Align the value to the right
            )
        }
    }
}
