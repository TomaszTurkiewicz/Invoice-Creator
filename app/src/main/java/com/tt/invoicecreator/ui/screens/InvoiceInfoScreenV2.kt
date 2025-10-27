package com.tt.invoicecreator.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.data.AppUiState
import com.tt.invoicecreator.data.roomV2.entities.InvoiceItemV2
import com.tt.invoicecreator.data.roomV2.entities.InvoiceV2
import com.tt.invoicecreator.data.roomV2.entities.PaidV2
import com.tt.invoicecreator.helpers.DateAndTime
import com.tt.invoicecreator.helpers.InvoiceNumber
import com.tt.invoicecreator.helpers.InvoiceValueCalculator
import com.tt.invoicecreator.ui.alert_dialogs.AlertDialogPayInvoiceV2
import com.tt.invoicecreator.ui.components.CustomButton
import com.tt.invoicecreator.ui.components.CustomCardView
import com.tt.invoicecreator.ui.components.PaymentHistoryRow
import com.tt.invoicecreator.ui.components.texts.BodyLargeText
import com.tt.invoicecreator.ui.components.texts.TitleLargeText
import com.tt.invoicecreator.ui.theme.Typography
import com.tt.invoicecreator.ui.theme.myColors
import com.tt.invoicecreator.viewmodel.AppViewModel

@Composable
fun InvoiceInfoScreenV2(
    invoiceV2: InvoiceV2,
//    invoiceItemListV2: List<InvoiceItemV2>,
    ignoredOnComposing:(AppBarState) -> Unit,
    viewModel: AppViewModel,
    uiState: AppUiState,
    navController: NavController,
    paidInvoicesCollection: List<PaidV2>?,
    invoiceItemsCollection: List<InvoiceItemV2>
    ) {

    val payAlertDialog = remember {
        mutableStateOf(false)
    }

    val invoiceItemListV2 = invoiceItemsCollection.filter {
        it.invoiceId == invoiceV2.invoiceId
    }

    val invoiceValue = remember {
        mutableDoubleStateOf(InvoiceValueCalculator.calculateV2(invoiceItemListV2))
    }

    val paidListV2 = paidInvoicesCollection?.filter {
        it.invoiceId == invoiceV2.invoiceId
    }



    LaunchedEffect(key1 = true) {
        ignoredOnComposing(
            AppBarState(
                title = "INFO",
                action = null
            )
        )
    }
    CustomCardView {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        )
        {
            TitleLargeText(
                text = "Invoice number: ${InvoiceNumber.getStringNumber(invoiceV2.invoiceNumber,invoiceV2.time)}",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .padding(top = 30.dp)
            ) {
                BodyLargeText(
                    text = "date: ${DateAndTime.convertLongToDate(invoiceV2.time)}",
                    modifier = Modifier
                        .padding(end = 10.dp)
                )
                if (invoiceV2.dueDate != null) {
                    BodyLargeText(
                        text = "due date: ${DateAndTime.convertLongToDate(invoiceV2.dueDate!!)}"
                    )
                }
            }

            Row(
                modifier = Modifier
                    .padding(top = 30.dp)
            ){
                BodyLargeText(
                    text = "Client:",
                    modifier = Modifier
                        .padding(end = 5.dp)
                )
                Column {
                    BodyLargeText(
                        text = invoiceV2.client.clientName
                    )
                    BodyLargeText(
                        text = invoiceV2.client.clientAddress1
                    )
                    BodyLargeText(
                        text = invoiceV2.client.clientAddress2
                    )
                    BodyLargeText(
                        text = invoiceV2.client.clientCity
                    )
                }
            }


            Row(
                modifier = Modifier
                    .padding(top = 30.dp)
            ) {
                BodyLargeText(
                    text = "Value: ${invoiceValue.doubleValue}",
                    modifier = Modifier
                        .padding(end = 20.dp)
                )
                if(InvoiceValueCalculator.calculatePaid(paidListV2)<InvoiceValueCalculator.calculateV2(invoiceItemListV2)){
                    BodyLargeText(
                        text = "Paid: ${InvoiceValueCalculator.calculatePaid(paidListV2)}"
                    )
                }else{
                    BodyLargeText(
                        text = "PAID IN FULL",
                        color = MaterialTheme.myColors.success
                    )
                }

            }


            BodyLargeText(
                text = "Payment History",
                modifier = Modifier
                    .padding(top = 30.dp, bottom = 10.dp)
            )

            paidListV2?.forEach { paidV2 ->
                PaymentHistoryRow(paidV2)
            }

            if(InvoiceValueCalculator.calculatePaid(paidListV2)<InvoiceValueCalculator.calculateV2(invoiceItemListV2)){
                CustomButton(
                    onClick = {
                        payAlertDialog.value = true
                    },
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .fillMaxWidth(),
                    text = "PAY"
                )
            }
        }
    }



    if(payAlertDialog.value){
        AlertDialogPayInvoiceV2(
            viewModel = viewModel,
            invoiceId = invoiceV2.invoiceId,
            paidListV2 = paidListV2,
            uiState = uiState,
            invoiceValue = invoiceValue.doubleValue,
            paidInvoicesCollection = paidInvoicesCollection,
            closeAlertDialog = {
                payAlertDialog.value = false
            }
        )
    }

}