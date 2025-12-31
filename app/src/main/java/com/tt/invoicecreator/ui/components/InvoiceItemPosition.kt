package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.roomV2.entities.InvoiceItemV2
import com.tt.invoicecreator.helpers.CurrencyFormatter
import com.tt.invoicecreator.helpers.InvoiceValueCalculator
import com.tt.invoicecreator.ui.components.texts.BodyLargeText
import com.tt.invoicecreator.ui.theme.myColors

@Composable
fun InvoiceItemPosition(
    itemInvoiceV2: InvoiceItemV2,
    border: Boolean,
    onEditClicked: () -> Unit
)
{
    Column(
        modifier = Modifier
            .padding(vertical = 5.dp)
            .border(
                width = if(border) 2.dp else 0.dp,
                color = if(border)
                    MaterialTheme
                    .myColors
                    .primaryDark
                else
                    MaterialTheme
                        .myColors
                        .material
                        .primaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically

        ){
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BodyLargeText(
                    text = CurrencyFormatter().format(
                        InvoiceValueCalculator.calculateV2oneTotalItem(itemInvoiceV2),
                        itemInvoiceV2.itemV2.itemCurrency
                    ),
                    modifier = Modifier
                        .padding(5.dp),
                    fontWeight = FontWeight.Bold
                )
            }
            CustomIconButton(
                onClick = {
                    onEditClicked()
                },
                imageVector = Icons.Default.Edit
            )
        }


        Row {
            BodyLargeText(
                text = itemInvoiceV2.itemV2.itemName,
                modifier = Modifier
                    .weight(3f)
                    .padding(5.dp)
            )
            BodyLargeText(
                text = itemInvoiceV2.itemCount.toString(),
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f),
                textAlign = TextAlign.End
            )
            BodyLargeText(
                text = "x",
                modifier = Modifier
                    .padding(5.dp)
                    .weight(0.5f),
                textAlign = TextAlign.Center

            )
            BodyLargeText(
                text = CurrencyFormatter().format(itemInvoiceV2.itemV2.itemValue, itemInvoiceV2.itemV2.itemCurrency),
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1.5f),
                textAlign = TextAlign.Start
            )
        }
        Row {
            BodyLargeText(
                text = "VAT",
                modifier = Modifier
                    .padding(5.dp)
            )
            BodyLargeText(
                text = ":",
                modifier = Modifier
                    .padding(5.dp)
            )
            BodyLargeText(
                text = if(itemInvoiceV2.vat != null) itemInvoiceV2.vat.toString() else "NO VAT",
                modifier = Modifier
                    .padding(5.dp)
            )
        }
        Row {
            BodyLargeText(
                text = "discount",
                modifier = Modifier
                    .padding(5.dp)
            )
            BodyLargeText(
                text = ":",
                modifier = Modifier
                    .padding(5.dp)
            )
            BodyLargeText(
                text = CurrencyFormatter().format(itemInvoiceV2.itemDiscount, itemInvoiceV2.itemV2.itemCurrency),
                modifier = Modifier
                    .padding(5.dp)
            )
        }
        if (itemInvoiceV2.comment != "") {
            BodyLargeText(
                text = itemInvoiceV2.comment,
                modifier = Modifier
                    .padding(5.dp)
            )
        }
    }
}