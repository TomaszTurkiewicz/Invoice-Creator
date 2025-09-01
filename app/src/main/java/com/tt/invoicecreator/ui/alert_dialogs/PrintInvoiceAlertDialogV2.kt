package com.tt.invoicecreator.ui.alert_dialogs

import android.content.Context
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
import com.tt.invoicecreator.data.roomV2.entities.InvoiceItemV2
import com.tt.invoicecreator.data.roomV2.entities.InvoiceV2
import com.tt.invoicecreator.helpers.InvoiceNumber
import com.tt.invoicecreator.helpers.PdfUtilsV2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintInvoiceAlertDialogV2(
    context: Context,
    invoiceV2: InvoiceV2,
    invoiceItemV2List: List<InvoiceItemV2>,
    onDismissRequest: () -> Unit,
    modePro: Boolean,
    goToInfo: () -> Unit
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
                text = "do You want to print invoice: ${InvoiceNumber.getStringNumber(invoiceNumber = invoiceV2.invoiceNumber, time = invoiceV2.time)} ?"
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){

                if(modePro){
                    Button(
                        onClick = {
                            goToInfo()
                            onDismissRequest()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                    ) {
                        Text(text = "INFO")
                    }
                }

                Button(
                    onClick = {
                        PdfUtilsV2.generatePdfV2(context = context, invoiceV2 = invoiceV2, items = invoiceItemV2List)
                        onDismissRequest()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    Text(text = "PRINT")
                }

                Button(
                    onClick = {
                        onDismissRequest()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    Text(text = "DISMISS")
                }
            }
        }

    }

}