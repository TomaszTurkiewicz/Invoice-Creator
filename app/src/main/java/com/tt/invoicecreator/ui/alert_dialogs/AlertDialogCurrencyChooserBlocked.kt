package com.tt.invoicecreator.ui.alert_dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.roomV2.entities.InvoiceItemV2
import com.tt.invoicecreator.ui.components.CustomButton
import com.tt.invoicecreator.ui.components.cards.CustomCardView
import com.tt.invoicecreator.ui.components.texts.BodyLargeText
import com.tt.invoicecreator.ui.components.texts.TitleLargeText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogCurrencyChooserBlocked(
    invoiceItemV2: InvoiceItemV2,
    onDismissRequest: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = {
            onDismissRequest()
        }
    ){
        CustomCardView {
            Column(
                modifier = Modifier
            ){
                TitleLargeText(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    text = "CURRENCY CHOOSER BLOCKED"
                )
                BodyLargeText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    text = "You already have item in Your invoice selected with ${invoiceItemV2.itemV2.itemCurrency.symbol}. You can only add another item with the same currency at this stage"
                )
                CustomButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    onClick = {
                        onDismissRequest()
                    },
                    enabled = true,
                    text = "OK"
                )
            }
        }
    }
}