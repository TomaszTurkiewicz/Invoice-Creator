package com.tt.invoicecreator.ui.alert_dialogs

import android.content.Context
import androidx.compose.runtime.Composable
import com.tt.invoicecreator.data.roomV2.InvoiceItemV2
import com.tt.invoicecreator.data.roomV2.InvoiceV2

@Composable
fun PrintInvoiceAlertDialogV2(
    context: Context,
    invoiceV2: InvoiceV2,
    invoiceItemV2List: List<InvoiceItemV2>,
    onDismissRequest: () -> Unit
) {

    val a = 1
}