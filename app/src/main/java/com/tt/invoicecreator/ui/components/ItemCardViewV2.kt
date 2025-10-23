package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.roomV2.entities.InvoiceItemV2
import com.tt.invoicecreator.ui.theme.Typography
import com.tt.invoicecreator.ui.theme.myColors

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
                    Text(
                        text =invoiceItemV2.itemV2.itemName,
                        fontWeight = FontWeight.W700,
                        style = Typography.bodyLarge,
                        color = MaterialTheme.myColors.primaryDark,
                        modifier = Modifier
                            .weight(3f)
                            .padding(5.dp)
                    )
                    Text(
                        text = invoiceItemV2.itemCount.toString(),
                        fontWeight = FontWeight.W500,
                        style = Typography.bodyLarge,
                        color = MaterialTheme.myColors.primaryDark,
                        modifier = Modifier
                            .padding(5.dp)
                            .weight(1f),
                        textAlign = TextAlign.End
                    )
                    Text(
                        text = "x",
                        fontWeight = FontWeight.W500,
                        style = Typography.bodyLarge,
                        color = MaterialTheme.myColors.primaryDark,
                        modifier = Modifier
                            .padding(5.dp)
                            .weight(0.5f),
                        textAlign = TextAlign.Center

                    )
                    Text(
                        text = invoiceItemV2.itemV2.itemValue.toString(),
                        fontWeight = FontWeight.W500,
                        style = Typography.bodyLarge,
                        color = MaterialTheme.myColors.primaryDark,
                        modifier = Modifier
                            .padding(5.dp)
                            .weight(1.5f),
                        textAlign = TextAlign.Start
                    )
                }
                Row {
                    Text(
                        text = "discount",
                        fontWeight = FontWeight.W500,
                        style = Typography.bodyLarge,
                        color = MaterialTheme.myColors.primaryDark,
                        modifier = Modifier
                            .padding(5.dp)
                    )
                    Text(
                        text = ":",
                        fontWeight = FontWeight.W500,
                        style = Typography.bodyLarge,
                        color = MaterialTheme.myColors.primaryDark,
                        modifier = Modifier
                            .padding(5.dp)
                    )
                    Text(
                        text = invoiceItemV2.itemDiscount.toString(),
                        fontWeight = FontWeight.W500,
                        style = Typography.bodyLarge,
                        color = MaterialTheme.myColors.primaryDark,
                        modifier = Modifier
                            .padding(5.dp)
                    )
                }
                if(invoiceItemV2.comment != ""){
                    Text(
                        text = invoiceItemV2.comment,
                        fontWeight = FontWeight.W500,
                        style = Typography.bodyLarge,
                        color = MaterialTheme.myColors.primaryDark,
                        modifier = Modifier
                            .padding(5.dp)
                    )
                }

            }
        }
}
}