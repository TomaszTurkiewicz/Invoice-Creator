package com.tt.invoicecreator.ui.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.VectorProperty
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.room.util.TableInfo
import com.tt.invoicecreator.InvoiceCreatorScreen
import com.tt.invoicecreator.MainActivity
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.data.InvoiceStatus
import com.tt.invoicecreator.data.SharedPreferences
import com.tt.invoicecreator.data.roomV2.entities.InvoiceItemV2
import com.tt.invoicecreator.data.roomV2.entities.InvoiceV2
import com.tt.invoicecreator.helpers.FilterInvoices
import com.tt.invoicecreator.ui.alert_dialogs.AlertDialogAddMainUser
import com.tt.invoicecreator.ui.alert_dialogs.AlertDialogSearchInvoices
import com.tt.invoicecreator.ui.alert_dialogs.AlertDialogWatchAd
import com.tt.invoicecreator.ui.alert_dialogs.PrintInvoiceAlertDialogV2
import com.tt.invoicecreator.ui.components.FilteredInvoicesCardView
import com.tt.invoicecreator.ui.components.ListOfInvoicesV2
import com.tt.invoicecreator.viewmodel.AppViewModel

@Composable
fun InvoicesScreenV2(
    viewModel: AppViewModel,
    ignoredOnComposing: (AppBarState) -> Unit,
    navController: NavController,
    modePro:Boolean,
    adLoaded:Boolean,
    activity: MainActivity,
    adWatched: Boolean,
    invoiceStatus: Enum<InvoiceStatus>
) {
    val invoiceListV2 by viewModel.invoiceListV2.observeAsState()

    val invoiceItemsCollection by viewModel.invoiceItemListV2.observeAsState()

    val paidInvoicesCollection by viewModel.paidListV2.observeAsState()

    val clientList by viewModel.clientListV2.observeAsState()


    val printInvoiceAlertDialog = remember {
        mutableStateOf(false)
    }

    val invoice = remember {
        mutableStateOf(InvoiceV2())
    }


    val itemList = remember {
        mutableListOf(InvoiceItemV2())
    }

    val addMainUSerAlertDialog = remember {
        mutableStateOf(false)
    }

    val watchAdAlertDialog = remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    val user = SharedPreferences.readUserDetails(context)

    val searchAlertDialog = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = true) {
        ignoredOnComposing(
            AppBarState(
                action = {
                    Row {
                        IconButton(onClick = {
                            viewModel.cleanInvoiceV2()
                            if (modePro){
                                navController.navigate(InvoiceCreatorScreen.AddInvoiceV2.name)
                            }
                            else{
                                activity.loadRewardedAd()
                                watchAdAlertDialog.value = true
                            }
                        }) {
                            Icon(Icons.Default.Add,null)
                        }

                        IconButton(onClick = {
                            navController.navigate(InvoiceCreatorScreen.Settings.name)
                        }) {
                            Icon(Icons.Default.Settings,null)
                        }
                    }
                }
            )
        )
    }

    LaunchedEffect(key1 = true) {

        if(user.userName == ""){
            addMainUSerAlertDialog.value = true
        }
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center){
        if(invoiceListV2.isNullOrEmpty()){
            Text(
                text = "press + on top of the app",
            )
        }
        else{
            if(invoiceItemsCollection.isNullOrEmpty()){
                Text(
                    text = "LOADING IN PROGRESS...",
                )
            }else{
                if(!modePro){
                ListOfInvoicesV2(
                    invoiceStatus = invoiceStatus,
                    itemList = invoiceItemsCollection!!,
                    list = invoiceListV2!!,
                    paidInvoices = paidInvoicesCollection,
                    invoiceChosen = {
                        invoice.value = it
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
                    modePro = false
                )
            }
                else{
                    Column() {
                        FilteredInvoicesCardView(
                            header = "ALL INVOICES",
                            message = "number of all invoices:",
                            count = invoiceListV2!!.size

                        ){
                            viewModel.updateInvoiceStatus(InvoiceStatus.ALL)
                            navController.navigate(InvoiceCreatorScreen.FilteredInvoicesV2.name)
                        }
                        FilteredInvoicesCardView(
                            header = "OVERDUE",
                            message = "number of overdue invoices:",
                            count = FilterInvoices.getOverdue(
                                invoiceListV2!!,
                                invoiceItemsCollection!!,
                                paidInvoicesCollection
                            ).size
                        ){
                            viewModel.updateInvoiceStatus(InvoiceStatus.OVERDUE)
                            navController.navigate(InvoiceCreatorScreen.FilteredInvoicesV2.name)
                        }
                        FilteredInvoicesCardView(
                            header = "NOT PAID",
                            message = "number of not paid invoices:",
                            count = FilterInvoices.getNotPaid(
                                invoiceListV2!!,
                                invoiceItemsCollection!!,
                                paidInvoicesCollection
                            ).size
                        ){
                            viewModel.updateInvoiceStatus(InvoiceStatus.NOT_PAID)
                            navController.navigate(InvoiceCreatorScreen.FilteredInvoicesV2.name)
                        }
                        FilteredInvoicesCardView(
                            header = "BY CLIENT",
                            message = "number of clients:",
                            count = clientList?.size ?: 0
                        ){
                            //todo
                        }
                    }
                }
            }

        }
    }

    if(addMainUSerAlertDialog.value){
        AlertDialogAddMainUser (
            user = user,
            closeAlertDialog = {
                addMainUSerAlertDialog.value = false
            },
            canBeDismissed = false
        )
             }

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

    if(watchAdAlertDialog.value){
        AlertDialogWatchAd(
            onDismissRequest = {
                watchAdAlertDialog.value = false
            },
            adLoaded = adLoaded
        ){
            activity.showRewardedAd()
        }

    }

    if(adWatched){
        navController.navigate(InvoiceCreatorScreen.AddInvoiceV2.name)
    }

    if(searchAlertDialog.value){
        AlertDialogSearchInvoices(
            viewModel = viewModel,
            listOfClients = clientList!!
        ) {
            searchAlertDialog.value = false
        }
    }
}

//todo: ALL, OVERDUE, NOT PAID, PAID, CLIENT