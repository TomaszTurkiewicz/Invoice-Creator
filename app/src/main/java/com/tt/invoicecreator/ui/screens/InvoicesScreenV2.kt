package com.tt.invoicecreator.ui.screens

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
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.data.roomV2.InvoiceV2
import com.tt.invoicecreator.ui.alert_dialogs.PrintInvoiceAlertDialog
import com.tt.invoicecreator.ui.components.ListOfInvoices
import com.tt.invoicecreator.ui.components.ListOfInvoicesV2
import com.tt.invoicecreator.viewmodel.AppViewModel

@Composable
fun InvoicesScreenV2(
    viewModel: AppViewModel,
    ignoredOnComposing: (AppBarState) -> Unit,
    navController: NavController
) {
    val invoiceListV2 by viewModel.invoiceListV2.observeAsState()

    val invoiceItemsCollection by viewModel.invoiceItemListV2.observeAsState()

    val printInvoiceAlertDialog = remember {
        mutableStateOf(false)
    }

    val invoice = remember {
        mutableStateOf(InvoiceV2())
    }

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        ignoredOnComposing(
            AppBarState(
                action = {
                    Row {
                        IconButton(onClick = {
                            viewModel.cleanInvoiceV2()
                            navController.navigate(InvoiceCreatorScreen.AddInvoiceV2.name)
                        }) {
                            Icon(Icons.Default.Add,null)
                        }
                        IconButton(onClick = {
                            // todo
                        }) {
                            Icon(Icons.Default.Settings,null)
                        }
                    }
                }
            )
        )
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
                viewModel = viewModel,
                itemList = invoiceItemsCollection!!,
                list = invoiceListV2!!,
                invoiceChosen = {
                    invoice.value = it
                    printInvoiceAlertDialog.value = true
                }
            )
        }
    }

//todo alert dialog printing invoice !!!
}