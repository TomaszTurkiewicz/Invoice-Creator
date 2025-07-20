package com.tt.invoicecreator.ui.alert_dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.room.Invoice
import com.tt.invoicecreator.helpers.InvoiceNumber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintInvoiceAlertDialog(
    invoice: Invoice,
    onDismissRequest:() -> Unit
) {

    BasicAlertDialog(
        onDismissRequest = {
            onDismissRequest()
        }
    ) {
        Column(
            modifier = Modifier
                .background(Color.LightGray)
        ){
            Text(
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(10.dp),
                text = "TITLE"
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                text = "do You want to print invoice: ${InvoiceNumber.getStringNumber(invoiceNumber = invoice.invoiceNumber, time = invoice.time)} ?"
            )

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Button(
                    onClick = {
                        onDismissRequest()
                    },
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(text = "DISMISS")
                }

                Button(
                    onClick = {
                        // todo
                    },
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(text = "PRINT")
                }

            }
        }

    }
}