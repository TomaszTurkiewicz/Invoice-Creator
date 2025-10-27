package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.roomV2.entities.InvoiceItemV2
import com.tt.invoicecreator.ui.components.texts.BodyLargeText

@Composable
fun ItemCardViewV2(
    invoiceItemV2: InvoiceItemV2
) {
    CustomCardView(
        modifier = Modifier
    ) {
        Row(
            modifier = Modifier
        )
        {
            Column {
                Row {
                    BodyLargeText(
                        text =invoiceItemV2.itemV2.itemName,
                        modifier = Modifier
                            .weight(3f)
                            .padding(5.dp)
                    )
                    BodyLargeText(
                        text = invoiceItemV2.itemCount.toString(),
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
                        text = invoiceItemV2.itemV2.itemValue.toString(),
                        modifier = Modifier
                            .padding(5.dp)
                            .weight(1.5f),
                        textAlign = TextAlign.Start
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
                        text = invoiceItemV2.itemDiscount.toString(),
                        modifier = Modifier
                            .padding(5.dp)
                    )
                }
                if(invoiceItemV2.comment != ""){
                    BodyLargeText(
                        text = invoiceItemV2.comment,
                        modifier = Modifier
                            .padding(5.dp)
                    )
                }

            }
        }
}
}