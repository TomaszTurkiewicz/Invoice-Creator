package com.tt.invoicecreator.ui.components.badge

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.helpers.Currency
import com.tt.invoicecreator.helpers.CurrencyFormatter
import com.tt.invoicecreator.ui.theme.myColors

@Composable
fun StatusBadge(
    isPaidInFull: Boolean,
    remaining: Double,
    currency: Currency,
    formatter: CurrencyFormatter
) {
    val backgroundColor =
        if (isPaidInFull) MaterialTheme.myColors.success else MaterialTheme.colorScheme.errorContainer
    val contentColor = if (isPaidInFull) Color.White else MaterialTheme.colorScheme.error
    val text = if (isPaidInFull) "PAID IN FULL" else "UNPAID (${
        formatter.format(
            remaining,
            currency
        )
    } remaining)"

    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        shape = CircleShape, // Pill shape
        modifier = Modifier.wrapContentSize()
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
    }
}