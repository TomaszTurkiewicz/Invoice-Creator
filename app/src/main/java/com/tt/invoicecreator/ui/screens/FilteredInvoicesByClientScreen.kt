package com.tt.invoicecreator.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.tt.invoicecreator.InvoiceCreatorScreen
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.data.roomV2.entities.ClientV2
import com.tt.invoicecreator.data.roomV2.entities.InvoiceItemV2
import com.tt.invoicecreator.data.roomV2.entities.InvoiceV2
import com.tt.invoicecreator.data.roomV2.entities.PaidV2
import com.tt.invoicecreator.helpers.FilterInvoices
import com.tt.invoicecreator.ui.components.DotShape
import com.tt.invoicecreator.ui.components.SingleRowInvoiceV2
import com.tt.invoicecreator.viewmodel.AppViewModel

@Composable
fun FilteredInvoicesByClientScreen(
    viewModel: AppViewModel,
    ignoredOnComposing: (AppBarState) -> Unit,
    navController: NavController,
    invoiceListV2: List<InvoiceV2>?,
    invoiceItemsCollection: List<InvoiceItemV2>?,
    paidInvoicesCollection: List<PaidV2>?
) {
    LaunchedEffect(key1 = true) {
        ignoredOnComposing(
            AppBarState(
                action = {
                    Row {
                        IconButton(onClick = {
                            viewModel.cleanInvoiceV2()
                            navController.navigate(InvoiceCreatorScreen.AddInvoiceV2.name)
                        }) {
                            Icon(Icons.Default.Add, null)
                        }

                        IconButton(onClick = {
                            navController.navigate(InvoiceCreatorScreen.Settings.name)
                        }) {
                            Icon(Icons.Default.Settings, null)
                        }
                    }
                }
            )
        )
    }

    val clientInvoices = FilterInvoices.getClientInvoices(invoiceListV2,viewModel.chosenClientToFilterInvoices!!.clientId)
    val clientInvoiceItems = FilterInvoices.getClientInvoiceItems(invoiceItemsCollection,clientInvoices)
    val clientPaid = FilterInvoices.getClientPaid(paidInvoicesCollection,clientInvoices)


    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.33f)
            ) {
                DotShape(enabled = true)
                Text(text = "ALL")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
            ) {
                DotShape(enabled = true)
                Text(text = "OVERDUE")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                DotShape(enabled = true)
                Text(text = "NOT PAID")
            }

        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(
                items = clientInvoices!!.reversed(),
            )
            { invoice ->
                val thisInvoiceItems =
                    FilterInvoices.getInvoiceItemsForInvoice(clientInvoiceItems, invoice.invoiceId)
                val thisPaid = FilterInvoices.getPaidItemsForInvoice(clientPaid, invoice.invoiceId)

                SingleRowInvoiceV2(
                    invoice = invoice,
                    invoiceItems = thisInvoiceItems!!,
                    paidInvoices = thisPaid,
                    invoiceChosen = {
                        //todo
                    },
                    itemsChosen = {
                        //todo
                    },
                    paidChosen = {
                        //todo
                    },
                    modePro = true
                )
            }
        }
    }
}