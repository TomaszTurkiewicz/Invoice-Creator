package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.tt.invoicecreator.data.roomV2.entities.PaidV2
import com.tt.invoicecreator.helpers.DateAndTime

@Composable
fun PaymentHistoryRow(
    paidV2: PaidV2
) {
    Row(

    ) {
        Text(
            text = DateAndTime.convertLongToDate(paidV2.time)
        )
        Text(
            text = "£"+paidV2.amountPaid.toString()
        )
        Text(
            text = paidV2.notes
        )
    }
}