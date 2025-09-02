package com.tt.invoicecreator.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.data.AppUiState
import com.tt.invoicecreator.data.roomV2.entities.InvoiceItemV2
import com.tt.invoicecreator.data.roomV2.entities.InvoiceV2
import com.tt.invoicecreator.data.roomV2.entities.PaidV2
import com.tt.invoicecreator.helpers.InvoiceNumber
import com.tt.invoicecreator.helpers.InvoiceValueCalculator
import com.tt.invoicecreator.ui.alert_dialogs.AlertDialogPayInvoiceV2
import com.tt.invoicecreator.viewmodel.AppViewModel

@Composable
fun InvoiceInfoScreenV2(
    invoiceV2: InvoiceV2,
    invoiceItemListV2: List<InvoiceItemV2>,
    paidListV2: List<PaidV2>?,
    ignoredOnComposing:(AppBarState) -> Unit,
    viewModel: AppViewModel,
    uiState: AppUiState,
    navController: NavController
    ) {

    val payAlertDialog = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = true) {
        ignoredOnComposing(
            AppBarState(
                action = null
            )
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Invoice number: ${InvoiceNumber.getStringNumber(invoiceV2.invoiceNumber,invoiceV2.time)}"
        )
        Text(
            text = "Client: ${invoiceV2.client.clientName} \n ${invoiceV2.client.clientAddress1} \n ${invoiceV2.client.clientAddress2} \n ${invoiceV2.client.clientCity}",
            modifier = Modifier.padding(bottom = 5.dp)
        )

        Text(
            text = "Value: ${InvoiceValueCalculator.calculateV2(invoiceItemListV2)}"
        )
        Text(
            text = "Paid: ${InvoiceValueCalculator.calculatePaid(paidListV2)}"
        )

        Button(
            onClick = {
                payAlertDialog.value = true
            }
        ) {
            Text(
                text = "PAY"
            )
        }

    }

    if(payAlertDialog.value){
        AlertDialogPayInvoiceV2(
            viewModel = viewModel,
            invoiceId = invoiceV2.invoiceId,
            paidListV2 = paidListV2,
            uiState = uiState,
            closeAlertDialog = {
                payAlertDialog.value = false
            }
        )
    }

}