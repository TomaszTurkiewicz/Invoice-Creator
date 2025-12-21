package com.tt.invoicecreator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
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
import com.tt.invoicecreator.MainActivity
import com.tt.invoicecreator.R
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.data.InvoiceStatus
import com.tt.invoicecreator.data.SharedPreferences
import com.tt.invoicecreator.data.roomV2.entities.ClientV2
import com.tt.invoicecreator.data.roomV2.entities.InvoiceItemV2
import com.tt.invoicecreator.data.roomV2.entities.InvoiceV2
import com.tt.invoicecreator.data.roomV2.entities.PaidV2
import com.tt.invoicecreator.helpers.FilterInvoices
import com.tt.invoicecreator.ui.alert_dialogs.AlertDialogAddMainUser
import com.tt.invoicecreator.ui.alert_dialogs.AlertDialogWatchAd
import com.tt.invoicecreator.ui.alert_dialogs.PrintInvoiceAlertDialogV2
import com.tt.invoicecreator.ui.components.ListOfInvoicesV2
import com.tt.invoicecreator.ui.components.cards.FilteredInvoicesCardView
import com.tt.invoicecreator.ui.components.texts.TitleLargeText
import com.tt.invoicecreator.viewmodel.AppViewModel

@Composable
fun InvoicesScreenV2(
    viewModel: AppViewModel,
    ignoredOnComposing: (AppBarState) -> Unit,
    navController: NavController,
    modePro: Boolean,
    adLoaded: Boolean,
    activity: MainActivity,
    adWatched: Boolean,
    invoiceStatus: Enum<InvoiceStatus>,
    invoiceListV2: List<InvoiceV2>?,
    invoiceItemsCollection: List<InvoiceItemV2>?,
    paidInvoicesCollection: List<PaidV2>?,
    clientList: List<ClientV2>?

) {

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

    LaunchedEffect(key1 = true) {
        ignoredOnComposing(
            AppBarState(
                title = if(!modePro) "ALL INVOICES" else "CHOOSE OPTION",
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
            TitleLargeText(
                text = "press + on top of the app",
            )
        }
        else{
            if(invoiceItemsCollection.isNullOrEmpty()){
                TitleLargeText(
                    text = "LOADING IN PROGRESS...",
                )
            }else{
                if(!modePro){
                ListOfInvoicesV2(
                    invoiceStatus = invoiceStatus,
                    itemList = invoiceItemsCollection,
                    list = invoiceListV2,
                    paidInvoices = paidInvoicesCollection,
                    invoiceChosen = { invoiceR, list ->
                        invoice.value = invoiceR
                        itemList.clear()
                        itemList.addAll(list)
                        viewModel.updateInvoiceV2(invoiceR)
                        printInvoiceAlertDialog.value = true
                    },
                    modePro = false
                )
            }
                else{
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        FilteredInvoicesCardView(
                            header = "ALL INVOICES",
                            message = "number of all invoices:",
                            count = invoiceListV2.size,
                            modifier = Modifier
                                .aspectRatio(3f)

                        ){
                            viewModel.updateInvoiceStatus(InvoiceStatus.ALL)
                            navController.navigate(InvoiceCreatorScreen.FilteredInvoicesV2.name)
                        }
                        FilteredInvoicesCardView(
                            header = "OVERDUE",
                            message = "number of overdue invoices:",
                            count = FilterInvoices.getOverdue(
                                invoiceListV2,
                                invoiceItemsCollection,
                                paidInvoicesCollection
                            ).size,
                            modifier = Modifier
                                .aspectRatio(3f)
                        ){
                            viewModel.updateInvoiceStatus(InvoiceStatus.OVERDUE)
                            navController.navigate(InvoiceCreatorScreen.FilteredInvoicesV2.name)
                        }
                        FilteredInvoicesCardView(
                            header = "NOT PAID",
                            message = "number of not paid invoices:",
                            count = FilterInvoices.getNotPaid(
                                invoiceListV2,
                                invoiceItemsCollection,
                                paidInvoicesCollection
                            ).size,
                            modifier = Modifier
                                .aspectRatio(3f)
                        ){
                            viewModel.updateInvoiceStatus(InvoiceStatus.NOT_PAID)
                            navController.navigate(InvoiceCreatorScreen.FilteredInvoicesV2.name)
                        }
                        FilteredInvoicesCardView(
                            header = "BY CLIENT",
                            message = "number of clients:",
                            count = clientList?.size ?: 0,
                            modifier = Modifier
                                .aspectRatio(3f)
                        ){  if(clientList.isNullOrEmpty()){
                            //do not do anything
                        }else{
                            navController.navigate(InvoiceCreatorScreen.InvoicesByClient.name)
                        }

                        }
                    }
                }
            }

        }
    }

    if(addMainUSerAlertDialog.value){
        AlertDialogAddMainUser (
            title = "ADD USER",
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
}