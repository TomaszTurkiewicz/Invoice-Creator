package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.ui.components.texts.BodyLargeText

@Composable
fun PaymentMethodCardView(
    onClick: () -> Unit,
    paymentMethod:String
) {


    CustomCardView(
        modifier = Modifier
            .clickable {
                onClick()
            }
    ) {
        Column(
            modifier = Modifier
        )
        {
            BodyLargeText(
                text = "Payment Method",
                modifier = Modifier
                    .padding(5.dp))
            BodyLargeText(
                text = paymentMethod,
                modifier = Modifier
                    .padding(5.dp))
        }
    }
}