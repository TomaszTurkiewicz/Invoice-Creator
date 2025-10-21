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
import com.tt.invoicecreator.ui.components.CustomCardView
import com.tt.invoicecreator.ui.components.PaymentHistoryRow
import com.tt.invoicecreator.ui.theme.Typography
import com.tt.invoicecreator.ui.theme.myColors
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

    val invoiceValue = remember {
        mutableDoubleStateOf(InvoiceValueCalculator.calculateV2(invoiceItemListV2))
    }

    val paidValue = remember {
        mutableDoubleStateOf(InvoiceValueCalculator.calculatePaid(paidListV2))
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
            Text(
                text = "Invoice number: ${InvoiceNumber.getStringNumber(invoiceV2.invoiceNumber,invoiceV2.time)}",
                style = Typography.titleLarge,
                color = MaterialTheme.myColors.primaryDark,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .padding(top = 30.dp)
            ) {
                Text(
                    text = "date: ${DateAndTime.convertLongToDate(invoiceV2.time)}",
                    style = Typography.bodyLarge,
                    color = MaterialTheme.myColors.primaryDark,
                    modifier = Modifier
                        .padding(end = 10.dp)
                )
                if (invoiceV2.dueDate != null) {
                    Text(
                        text = "due date: ${DateAndTime.convertLongToDate(invoiceV2.dueDate!!)}",
                        style = Typography.bodyLarge,
                        color = MaterialTheme.myColors.primaryDark
                    )
                }
            }

            Row(
                modifier = Modifier
                    .padding(top = 30.dp)
            ){
                Text(
                    text = "Client:",
                    style = Typography.bodyLarge,
                    color = MaterialTheme.myColors.primaryDark,
                    modifier = Modifier
                        .padding(end = 5.dp)
                )
                Column {
                    Text(
                        text = invoiceV2.client.clientName,
                        style = Typography.bodyLarge,
                        color = MaterialTheme.myColors.primaryDark
                    )
                    Text(
                        text = invoiceV2.client.clientAddress1,
                        style = Typography.bodyLarge,
                        color = MaterialTheme.myColors.primaryDark
                    )
                    Text(
                        text = invoiceV2.client.clientAddress2,
                        style = Typography.bodyLarge,
                        color = MaterialTheme.myColors.primaryDark
                    )
                    Text(
                        text = invoiceV2.client.clientCity,
                        style = Typography.bodyLarge,
                        color = MaterialTheme.myColors.primaryDark
                    )
                }
            }


            Row(
                modifier = Modifier
                    .padding(top = 30.dp)
            ) {
                Text(
                    text = "Value: ${invoiceValue.doubleValue}",
                    style = Typography.bodyLarge,
                    color = MaterialTheme.myColors.primaryDark,
                    modifier = Modifier
                        .padding(end = 20.dp)
                )
                if(InvoiceValueCalculator.calculatePaid(paidListV2)<InvoiceValueCalculator.calculateV2(invoiceItemListV2)){
                    Text(
                        text = "Paid: ${InvoiceValueCalculator.calculatePaid(paidListV2)}",
                        style = Typography.bodyLarge,
                        color = MaterialTheme.myColors.primaryDark
                    )
                }else{
                    Text(
                        text = "PAID IN FULL",
                        style = Typography.bodyLarge,
                        color = MaterialTheme.myColors.success
                    )
                }

            }


            Text(
                text = "Payment History",
                style = Typography.bodyLarge,
                color = MaterialTheme.myColors.primaryDark,
                modifier = Modifier
                    .padding(top = 30.dp, bottom = 10.dp)
            )

            paidListV2?.forEach { paidV2 ->
                PaymentHistoryRow(paidV2)
            }

            if(InvoiceValueCalculator.calculatePaid(paidListV2)<InvoiceValueCalculator.calculateV2(invoiceItemListV2)){
                Button(
                    onClick = {
                        payAlertDialog.value = true
                    },
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "PAY"
                    )
                }
            }
//        Button(
//            onClick = {
//                payAlertDialog.value = true
//            }
//        ) {
//            Text(
//                text = "PAY"
//            )
//        }

        }
    }



    if(payAlertDialog.value){
        AlertDialogPayInvoiceV2(
            viewModel = viewModel,
            invoiceId = invoiceV2.invoiceId,
            paidListV2 = paidListV2,
            uiState = uiState,
            invoiceValue = invoiceValue.doubleValue,
            paid = paidValue.doubleValue,
            closeAlertDialog = {
                payAlertDialog.value = false
            }
        )
    }

}