package com.tt.invoicecreator.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.tt.invoicecreator.InvoiceCreatorScreen
import com.tt.invoicecreator.MainActivity
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.data.SharedPreferences
import com.tt.invoicecreator.data.roomV2.InvoiceItemV2
import com.tt.invoicecreator.data.roomV2.InvoiceV2
import com.tt.invoicecreator.ui.alert_dialogs.AlertDialogAddMainUser
import com.tt.invoicecreator.ui.alert_dialogs.AlertDialogWatchAd
import com.tt.invoicecreator.ui.alert_dialogs.PrintInvoiceAlertDialogV2
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
    adWatched: Boolean
) {
    val invoiceListV2 by viewModel.invoiceListV2.observeAsState()

    val invoiceItemsCollection by viewModel.invoiceItemListV2.observeAsState()


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
        .fillMaxSize(),
        contentAlignment = Alignment.Center){
        if(invoiceListV2.isNullOrEmpty()){
            Text(
                text = "press + on top of the app",
            )
        }
        else{
            ListOfInvoicesV2(
                itemList = invoiceItemsCollection!!,
                list = invoiceListV2!!,
                invoiceChosen = {
                    invoice.value = it
                },
                itemsChosen = {
                    val size = it.size
                    itemList.clear()
                    for(i in 0..size){
                        if(i<size){
                            itemList.add(it[i])
                        }else{
                            printInvoiceAlertDialog.value = true
                        }
                    }
                }
            )
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