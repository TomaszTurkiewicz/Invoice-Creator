package com.tt.invoicecreator.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.tt.invoicecreator.ui.alert_dialogs.PrintInvoiceAlertDialogV2
import com.tt.invoicecreator.ui.components.ListOfInvoicesV2
import com.tt.invoicecreator.viewmodel.AppViewModel

@Composable
fun FilteredInvoicesScreen(
    viewModel: AppViewModel,
    ignoredOnComposing: (AppBarState) -> Unit,
    navController: NavController,
    invoiceStatus: Enum<InvoiceStatus>,
    modePro: Boolean,
    invoiceItemsCollection: List<InvoiceItemV2>?,
    invoiceListV2: List<InvoiceV2>?,
    paidInvoicesCollection: List<PaidV2>?
) {

    val invoice = remember {
        mutableStateOf(InvoiceV2())
    }

    val itemList = remember {
        mutableListOf(InvoiceItemV2())
    }

    val printInvoiceAlertDialog = remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        ignoredOnComposing(
            AppBarState(
                title = when(invoiceStatus){
                    InvoiceStatus.ALL -> "ALL INVOICES"
                    InvoiceStatus.OVERDUE -> "OVERDUE INVOICES"
                    InvoiceStatus.NOT_PAID -> "NOT PAID INVOICES"
                    else -> "ALL INVOICES"
                },
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

    ListOfInvoicesV2(
        invoiceStatus = invoiceStatus,
        itemList = invoiceItemsCollection!!,
        list = invoiceListV2!!,
        paidInvoices = paidInvoicesCollection,
        invoiceChosen = { invoiceR, list ->
            invoice.value = invoiceR
            viewModel.updateInvoiceV2(invoiceR)
            itemList.clear()
            itemList.addAll(list)
            printInvoiceAlertDialog.value = true
        },
        modePro = true



    )

    if(printInvoiceAlertDialog.value){
        PrintInvoiceAlertDialogV2(
            context = context,
            invoiceV2 = invoice.value,
            invoiceItemV2List = itemList,
            onDismissRequest = {
                printInvoiceAlertDialog.value = false
            },
            modePro = modePro,
            goToInfo = {
                navController.navigate(InvoiceCreatorScreen.InvoiceInfoV2.name)
            }
        )
    }
}