package com.tt.invoicecreator.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.tt.invoicecreator.InvoiceCreatorScreen
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.data.InvoiceStatus
import com.tt.invoicecreator.data.roomV2.entities.ClientV2
import com.tt.invoicecreator.data.roomV2.entities.InvoiceItemV2
import com.tt.invoicecreator.data.roomV2.entities.InvoiceV2
import com.tt.invoicecreator.data.roomV2.entities.PaidV2
import com.tt.invoicecreator.helpers.FilterInvoices
import com.tt.invoicecreator.ui.alert_dialogs.PrintInvoiceAlertDialogV2
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
                title = viewModel.chosenClientToFilterInvoices?.clientName ?: "",
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
    val invoiceMutable = remember {
        mutableStateOf(InvoiceV2())
    }

    val itemList = remember {
        mutableListOf(InvoiceItemV2())
    }

    val invoiceFilterState = remember {
        mutableStateOf(InvoiceStatus.ALL)
    }

    val clientInvoices = FilterInvoices.getClientInvoices(invoiceListV2,viewModel.chosenClientToFilterInvoices!!.clientId)
    val clientInvoiceItems = FilterInvoices.getClientInvoiceItems(invoiceItemsCollection,clientInvoices)
    val clientPaid = FilterInvoices.getClientPaid(paidInvoicesCollection,clientInvoices)

    val newClientList = when(invoiceFilterState.value){
        InvoiceStatus.ALL -> clientInvoices
        InvoiceStatus.OVERDUE -> FilterInvoices.getOverdue(clientInvoices!!,clientInvoiceItems!!,clientPaid)
        InvoiceStatus.NOT_PAID -> FilterInvoices.getNotPaid(clientInvoices!!,clientInvoiceItems!!,clientPaid)
        else -> clientInvoices

    }

    val printInvoiceAlertDialog = remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current


    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.33f)
                    .clickable{
                        invoiceFilterState.value = InvoiceStatus.ALL
                    }
            ) {
                DotShape(enabled = invoiceFilterState.value == InvoiceStatus.ALL)
                Text(text = "ALL")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .clickable{
                        invoiceFilterState.value = InvoiceStatus.OVERDUE
                    }
            ) {
                DotShape(enabled = invoiceFilterState.value == InvoiceStatus.OVERDUE)
                Text(text = "OVERDUE")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable{
                        invoiceFilterState.value = InvoiceStatus.NOT_PAID
                    }
            ) {
                DotShape(enabled = invoiceFilterState.value == InvoiceStatus.NOT_PAID)
                Text(text = "NOT PAID")
            }

        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(
                items = newClientList!!.reversed(),
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
                        invoiceMutable.value = it
                        viewModel.updateInvoiceV2(it)
                    },
                    itemsChosen = {
                        val size = it.size
                        itemList.clear()
                        for(i in 0..size){
                            if(i<size){
                                itemList.add(it[i])
                            }else{
                                viewModel.updateInvoiceItemListV2(it)
                                printInvoiceAlertDialog.value = true
                            }
                        }
                    },
                    paidChosen = {
                        viewModel.updatePaidListV2(it)
                    },
                    modePro = true
                )
            }
        }
    }

    if(printInvoiceAlertDialog.value){

        PrintInvoiceAlertDialogV2(
            context = context,
            invoiceV2 = invoiceMutable.value,
            invoiceItemV2List = itemList,
            onDismissRequest = {
                printInvoiceAlertDialog.value = false
            },
            modePro = true,
            goToInfo = {
                navController.navigate(InvoiceCreatorScreen.InvoiceInfoV2.name)
            }
        )
    }
}