package com.tt.invoicecreator.ui.alert_dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.InvoiceStatus
import com.tt.invoicecreator.ui.components.CustomCardView
import com.tt.invoicecreator.ui.components.InvoiceStateRow
import com.tt.invoicecreator.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogSearchInvoices(
    viewModel: AppViewModel,
    closeAlertDialog: () -> Unit,
) {
    val invoiceStatus = remember {
        mutableStateOf(InvoiceStatus.ALL)
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




                Button(
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