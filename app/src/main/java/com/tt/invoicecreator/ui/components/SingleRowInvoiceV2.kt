package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.room.Invoice
import com.tt.invoicecreator.data.roomV2.InvoiceItemV2
import com.tt.invoicecreator.data.roomV2.InvoiceV2
import com.tt.invoicecreator.helpers.DateAndTime
import com.tt.invoicecreator.helpers.InvoiceNumber
import com.tt.invoicecreator.helpers.InvoiceValueCalculator

@Composable
fun SingleRowInvoiceV2(
    invoice: InvoiceV2,
    invoiceItems:List<InvoiceItemV2>,
    invoiceChosen: (InvoiceV2) -> Unit,
    itemsChosen: (List<InvoiceItemV2>) -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                invoiceChosen(invoice)
                itemsChosen(invoiceItems)
            },
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ){
            Text(
//                text = "number: ${invoice.invoiceNumber}"
                text = "number: ${InvoiceNumber.getStringNumber(invoice.invoiceNumber,invoice.time)}"
            )
            Text(
                text = "date: ${DateAndTime.convertLongToDate(invoice.time)}"
            )
            Text(
                text = "client: ${invoice.client.clientName}"
            )
            Text(
//                text = "value: ${InvoiceValueCalculator.calculate(invoice)}"
                text = "value: ${InvoiceValueCalculator.calculateV2(invoiceItems)}"
            )
        }

        val a = 1
    }
}