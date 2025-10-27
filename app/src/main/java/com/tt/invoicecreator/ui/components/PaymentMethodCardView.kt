package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.ui.components.texts.BodyLargeText
import com.tt.invoicecreator.ui.theme.Typography
import com.tt.invoicecreator.ui.theme.myColors

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

@Composable
@Preview
fun ShowPaymentMethodCardView() {
    PaymentMethodCardView(
        onClick = {},
        paymentMethod = "Cash"
    )
}