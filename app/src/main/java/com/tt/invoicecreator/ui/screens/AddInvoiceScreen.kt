package com.tt.invoicecreator.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tt.invoicecreator.InvoiceCreatorScreen
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.helpers.InvoiceNumber
import com.tt.invoicecreator.ui.alert_dialogs.AlertDialogInvoiceNumber
import com.tt.invoicecreator.ui.components.ClientCardView
import com.tt.invoicecreator.ui.components.InvoiceNumberCardView
import com.tt.invoicecreator.ui.components.ItemCardView
import com.tt.invoicecreator.ui.components.PaymentMethodCardView
import com.tt.invoicecreator.viewmodel.AppViewModel

@Composable
fun AddInvoiceScreen(
    viewModel: AppViewModel,
    ignoredOnComposing: (AppBarState) -> Unit,
    navController: NavController
) {
    val invoiceList by viewModel.invoiceList.observeAsState()


    val time = remember {
        mutableLongStateOf(0L)
    }
//    val invoiceNumber = remember {
//        mutableIntStateOf(viewModel.getInvoice().invoiceNumber)
//    }

    val invoiceNumberAlertDialog = remember {
        mutableStateOf(false)
    }


    LaunchedEffect(key1 = true) {
        ignoredOnComposing(
            AppBarState(
                action = {
                    Row {
                        IconButton(onClick = {
                            //todo
                        }) {
                            Icon(Icons.Default.Settings,null)
                        }
                    }

                }
            )
        )
    }

    LaunchedEffect(key1 = true) {
        time.longValue = System.currentTimeMillis()
        viewModel.getInvoice().time = time.longValue

        if(viewModel.calculateNumber){
            viewModel.getInvoice().invoiceNumber= InvoiceNumber.getNewNumber(viewModel.getInvoice().time,invoiceList)
        }


    }


    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        InvoiceNumberCardView(
            number = viewModel.getInvoice().invoiceNumber,
            time = time.longValue
        ){
            invoiceNumberAlertDialog.value = true
        }

        ClientCardView(
            viewModel = viewModel,
            onClick = {
                navController.navigate(InvoiceCreatorScreen.ChooseClient.name)
            }
        )

        ItemCardView(
            viewModel = viewModel,
            onClick = {
                navController.navigate(InvoiceCreatorScreen.ChooseItem.name)
            }
        )

        PaymentMethodCardView()

        Button(
            onClick ={
                //todo
            },
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
        ) {
            Text(text = "SAVE")
        }
    }


    if(invoiceNumberAlertDialog.value){
        AlertDialogInvoiceNumber(
            onDismissRequest = {
                invoiceNumberAlertDialog.value = false
            },
            viewModel = viewModel
        )
    }
}