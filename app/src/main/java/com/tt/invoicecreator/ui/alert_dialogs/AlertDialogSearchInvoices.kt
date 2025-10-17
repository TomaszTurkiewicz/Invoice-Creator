package com.tt.invoicecreator.ui.alert_dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.InvoiceStatus
import com.tt.invoicecreator.data.roomV2.entities.ClientV2
import com.tt.invoicecreator.ui.components.CustomCardView
import com.tt.invoicecreator.ui.components.DotShape
import com.tt.invoicecreator.ui.components.InputTextWithLabel
import com.tt.invoicecreator.ui.components.InputTextWithLazyColumn
import com.tt.invoicecreator.ui.components.InvoiceStateRow
import com.tt.invoicecreator.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogSearchInvoices(
    viewModel: AppViewModel,
    listOfClients: List<ClientV2>,
    closeAlertDialog: () -> Unit,
) {
    val invoiceStatus = remember {
        mutableStateOf(InvoiceStatus.ALL)
    }

    val searchByClient = remember {
        mutableStateOf(false)
    }

    val clientName = remember {
        mutableStateOf("")
    }

    val client = remember {
        mutableStateOf(ClientV2())
    }

    BasicAlertDialog(
        onDismissRequest = {
            closeAlertDialog()
        }
    ) {
        CustomCardView {
            Column(
                modifier = Modifier
            ){
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    text = "SHOW INVOICES"
                )
                InvoiceStateRow(invoiceStatus.value,InvoiceStatus.ALL){
                    invoiceStatus.value = it
                }
                InvoiceStateRow(invoiceStatus.value,InvoiceStatus.OVERDUE){
                    invoiceStatus.value = it
                }
                InvoiceStateRow(invoiceStatus.value,InvoiceStatus.NOT_PAID){
                    invoiceStatus.value = it
                }
                InvoiceStateRow(invoiceStatus.value,InvoiceStatus.PAID){
                    invoiceStatus.value = it
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .clickable(
                            onClick = {
                                searchByClient.value = !searchByClient.value
                            }
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DotShape(
                        searchByClient.value
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 10.dp),
                        text = "FILTER BY CLIENT"
                    )
                }

                if(searchByClient.value){
                    InputTextWithLazyColumn(
                        labelText = "Type client name here",
                        inputText = clientName.value,
                        onValueChange = {
                            clientName.value = it
                        },
                        listOfClients = listOfClients,
                        chooseClient = {
                            client.value = it
                        }
                    )
                }

                // todo add input text to search client

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
                    onClick = {
                        viewModel.updateInvoiceStatus(invoiceStatus.value)
                        closeAlertDialog()
                    }
                ) { Text(
                    text = "OK"
                )}
            }
        }
    }
}

// todo finish this first !!!!!!!