package com.tt.invoicecreator.ui.alert_dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.InvoiceStatus
import com.tt.invoicecreator.ui.components.CustomCardView
import com.tt.invoicecreator.ui.components.InvoiceStateRow
import com.tt.invoicecreator.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogSearchInvoices(
    invoiceStatus: Enum<InvoiceStatus>,
    viewModel: AppViewModel,
    closeAlertDialog: () -> Unit,
) {
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
                InvoiceStateRow(invoiceStatus,InvoiceStatus.ALL){
                    viewModel.updateInvoiceStatus(it)
                }
                InvoiceStateRow(invoiceStatus,InvoiceStatus.OVERDUE){
                    viewModel.updateInvoiceStatus(it)
                }
                InvoiceStateRow(invoiceStatus,InvoiceStatus.NOT_PAID){
                    viewModel.updateInvoiceStatus(it)
                }
                InvoiceStateRow(invoiceStatus,InvoiceStatus.PAID){
                    viewModel.updateInvoiceStatus(it)
                }


                Button(
                    onClick = {
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