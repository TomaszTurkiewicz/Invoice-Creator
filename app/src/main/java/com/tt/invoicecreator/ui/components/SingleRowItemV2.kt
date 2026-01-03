package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.roomV2.entities.ItemV2
import com.tt.invoicecreator.helpers.CurrencyFormatter
import com.tt.invoicecreator.ui.components.cards.CustomCardView
import com.tt.invoicecreator.ui.components.texts.BodyLargeText

@Composable
fun SingleRowItemV2(
    item: ItemV2,
    itemChosen: (ItemV2) -> Unit,
    onEditClicked: (ItemV2) -> Unit
) {

    val newFormattedValue = CurrencyFormatter().format(item.itemValue, item.itemCurrency)


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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BodyLargeText(
                text = item.itemName,
                modifier = Modifier
                    .weight(1f) // Use weight for flexible sizing
                    .padding(5.dp)
            )
            BodyLargeText(
                text = newFormattedValue, // Use the new formatted value text
                modifier = Modifier
                    .padding(5.dp),
                textAlign = TextAlign.End // Align the value to the right
            )

            CustomIconButton(
                onClick = {
                    onEditClicked(item)
                },
                imageVector = Icons.Default.Edit,
                modifier = Modifier
                    .padding(8.dp)
            )
        }
    }
}
