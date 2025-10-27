package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.roomV2.entities.PaidV2
import com.tt.invoicecreator.helpers.DateAndTime
import com.tt.invoicecreator.ui.components.texts.BodyLargeText
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
        BodyLargeText(
            text = DateAndTime.convertLongToDate(paidV2.time),
            modifier = Modifier
                .padding(end = 10.dp)
//            modifier = Modifier
//                .weight(1f)
        )
        BodyLargeText(
            text = "£"+paidV2.amountPaid.toString(),
            modifier = Modifier
                .weight(1f),
            textAlign = TextAlign.Start
        )
        BodyLargeText(
            text = paidV2.notes,
            modifier = Modifier
                .weight(2f)
        )
    }
}