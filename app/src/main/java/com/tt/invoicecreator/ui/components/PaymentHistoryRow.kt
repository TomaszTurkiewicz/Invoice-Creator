package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.roomV2.entities.PaidV2
import com.tt.invoicecreator.helpers.DateAndTime
import com.tt.invoicecreator.ui.theme.Typography
import com.tt.invoicecreator.ui.theme.myColors

@Composable
fun PaymentHistoryRow(
    paidV2: PaidV2
) {
    Row(
        modifier = Modifier
            .padding(bottom = 5.dp)
    ) {
        Text(
            text = DateAndTime.convertLongToDate(paidV2.time),
            style = Typography.bodyLarge,
            color = MaterialTheme.myColors.primaryDark,
            modifier = Modifier
                .weight(1f)
        )
        Text(
            text = "£"+paidV2.amountPaid.toString(),
            style = Typography.bodyLarge,
            color = MaterialTheme.myColors.primaryDark,
            modifier = Modifier
                .weight(1f),
            textAlign = TextAlign.Start
        )
        Text(
            text = paidV2.notes,
            style = Typography.bodyLarge,
            color = MaterialTheme.myColors.primaryDark,
            modifier = Modifier
                .weight(1f
                )
        )
    }
}