package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tt.invoicecreator.data.roomV2.InvoiceV2

@Composable
fun ListOfInvoicesV2(
    list:List<InvoiceV2>,
    invoiceChosen: (InvoiceV2) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(
            items = list.reversed()
        ){
            invoice ->
            SingleRowInvoiceV2(
                invoice = invoice,
                invoiceChosen = {
                    invoiceChosen(it)
                }
            )
        }
    }
}