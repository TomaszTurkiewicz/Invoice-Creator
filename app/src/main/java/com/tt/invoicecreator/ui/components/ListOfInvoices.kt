package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tt.invoicecreator.data.room.Invoice

@Composable
fun ListOfInvoices(
    list:List<Invoice>,
    invoiceChosen: (Invoice) -> Unit
) {
//    val newList = list.reversed()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(
            items = list.reversed()
        ){
            invoice ->
            SingleRowInvoice(
                invoice = invoice,
                invoiceChosen = {
                    invoiceChosen(it)
                }
            )
        }
    }
}