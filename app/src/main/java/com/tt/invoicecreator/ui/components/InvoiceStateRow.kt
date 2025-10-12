package com.tt.invoicecreator.ui.components

import android.text.Layout
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.InvoiceStatus


@Composable
fun InvoiceStateRow(
    invoiceStatus: Enum<InvoiceStatus>,
    position: InvoiceStatus,
    onClick: (InvoiceStatus) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable(
                onClick = {
                    onClick(position)
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DotShape(
            invoiceStatus == position
        )
        Text(
            modifier = Modifier
                .padding(start = 10.dp),
            text = position.toString()
        )
    }
}