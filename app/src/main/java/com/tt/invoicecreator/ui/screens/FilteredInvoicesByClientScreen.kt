package com.tt.invoicecreator.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.tt.invoicecreator.InvoiceCreatorScreen
import com.tt.invoicecreator.R
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.data.InvoiceStatus
import com.tt.invoicecreator.data.roomV2.entities.InvoiceItemV2
import com.tt.invoicecreator.data.roomV2.entities.InvoiceV2
import com.tt.invoicecreator.data.roomV2.entities.PaidV2
import com.tt.invoicecreator.helpers.FilterInvoices
import com.tt.invoicecreator.ui.alert_dialogs.PrintInvoiceAlertDialogV2
import com.tt.invoicecreator.ui.components.DotShape
import com.tt.invoicecreator.ui.components.SingleRowInvoiceV2
import com.tt.invoicecreator.ui.components.texts.BodyLargeText
import com.tt.invoicecreator.ui.theme.myColors
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
                pro = true,
                title = viewModel.chosenClientToFilterInvoices?.clientName ?: "",
                action = {
                    Row {
                        IconButton(onClick = {
                            viewModel.cleanInvoiceV2()
                            navController.navigate(InvoiceCreatorScreen.AddInvoiceV2.name)
                        }) {
                            Icon(painter = painterResource(R.drawable.baseline_add_24), null)
                        }

                        IconButton(onClick = {
                            navController.navigate(InvoiceCreatorScreen.Settings.name)
                        }) {
                            Icon(painter = painterResource(R.drawable.baseline_settings_24), null)
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
                DotShape(
                    enabled = invoiceFilterState.value == InvoiceStatus.ALL,
                    color = MaterialTheme.myColors.primaryDark)
                BodyLargeText(
                    text = "ALL",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                    )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .clickable{
                        invoiceFilterState.value = InvoiceStatus.OVERDUE
                    }
            ) {
                DotShape(
                    enabled = invoiceFilterState.value == InvoiceStatus.OVERDUE,
                color = MaterialTheme.myColors.primaryDark)
                BodyLargeText(
                    text = "OVERDUE",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable{
                        invoiceFilterState.value = InvoiceStatus.NOT_PAID
                    }
            ) {
                DotShape(enabled = invoiceFilterState.value == InvoiceStatus.NOT_PAID,
                    color = MaterialTheme.myColors.primaryDark)
                BodyLargeText(
                    text = "NOT PAID",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
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
                    invoiceChosen = { invoiceR, list ->
                        invoiceMutable.value = invoiceR
                        viewModel.updateInvoiceV2(invoiceR)
                        itemList.clear()
                        itemList.addAll(list)
                        printInvoiceAlertDialog.value = true
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