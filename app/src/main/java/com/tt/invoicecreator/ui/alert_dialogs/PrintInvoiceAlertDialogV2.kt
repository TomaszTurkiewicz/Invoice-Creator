package com.tt.invoicecreator.ui.alert_dialogs

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.roomV2.entities.InvoiceItemV2
import com.tt.invoicecreator.data.roomV2.entities.InvoiceV2
import com.tt.invoicecreator.helpers.InvoiceNumber
import com.tt.invoicecreator.helpers.PdfUtilsV2
import com.tt.invoicecreator.ui.components.CustomButton
import com.tt.invoicecreator.ui.components.cards.CustomCardView
import com.tt.invoicecreator.ui.components.texts.BodyLargeText
import com.tt.invoicecreator.ui.components.texts.TitleLargeText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    val scope = rememberCoroutineScope()


    BasicAlertDialog(
        onDismissRequest = {
            onDismissRequest()
        }
    ) {
        CustomCardView {
            Column(
                modifier = Modifier
            )
            {
                TitleLargeText(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    text = "INVOICE: ${
                    InvoiceNumber.getStringNumber(
                        invoiceNumber = invoiceV2.invoiceNumber,
                        time = invoiceV2.time
                    )
                }"
                )
                BodyLargeText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    text = "You can print this invoice out. It will be saved in Download folder on Your device. Press PRINT button below. If You are using PRO version, you can also press INFO button below. You will be redirected to invoice info page, where You can set payment, etc."
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    if (modePro) {
                        CustomButton(
                            onClick = {
                                goToInfo()
                                onDismissRequest()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            text = "INFO"
                        )
                    }

                    CustomButton(
                        onClick = {
                            scope.launch(Dispatchers.IO) {
                                PdfUtilsV2.generateInvoicePdfV2(
                                    context = context,
                                    invoiceV2 = invoiceV2,
                                    items = invoiceItemV2List
                                )
                                withContext(Dispatchers.Main){
                                    onDismissRequest()
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        text = "PRINT"
                    )

//                    CustomButton(
//                        onClick = {
//                            onDismissRequest()
//                        },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(top = 10.dp, bottom = 10.dp),
//                        text = "DISMISS"
//                    )
                }
            }
        }

    }

}