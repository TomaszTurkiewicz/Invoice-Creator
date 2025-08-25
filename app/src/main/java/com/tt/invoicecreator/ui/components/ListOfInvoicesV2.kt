package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tt.invoicecreator.data.roomV2.InvoiceItemV2
import com.tt.invoicecreator.data.roomV2.InvoiceV2

@Composable
fun ListOfInvoicesV2(
    list:List<InvoiceV2>,
    itemList:List<InvoiceItemV2>,
    invoiceChosen: (InvoiceV2) -> Unit,
    itemsChosen: (List<InvoiceItemV2>) -> Unit
) {


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(
            items = list.reversed()
        ){
            invoice ->
            val invoiceItems = itemList.filter {
                it.invoiceId == invoice.invoiceId
            }

            SingleRowInvoiceV2(
                invoice = invoice,
                invoiceItems = invoiceItems,
                invoiceChosen = {
                    invoiceChosen(it)
                },
                itemsChosen = {
                    itemsChosen(it)
                }
            )
        }
    }
}