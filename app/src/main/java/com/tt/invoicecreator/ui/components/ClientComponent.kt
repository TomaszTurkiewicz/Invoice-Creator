package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.ui.components.texts.BodyLargeText
import com.tt.invoicecreator.viewmodel.AppViewModel

@Composable
fun ClientComponent(
    viewModel: AppViewModel,
    onClick: () -> Unit
)
{
    if (viewModel.getInvoiceV2().client.clientName != "") {
        Column(
            modifier = Modifier
                .clickable(
                    onClick = {
                       onClick()
                    }
                )
        ) {
            BodyLargeText(
                text = viewModel.getInvoiceV2().client.clientAddress1,
                modifier = Modifier
                    .padding(start = 10.dp, top = 5.dp, bottom = 5.dp)
            )
            BodyLargeText(
                text = viewModel.getInvoiceV2().client.clientAddress2,
                modifier = Modifier
                    .padding(start = 10.dp, top = 5.dp, bottom = 5.dp)
            )
            BodyLargeText(
                text = viewModel.getInvoiceV2().client.clientCity,
                modifier = Modifier
                    .padding(start = 10.dp, top = 5.dp, bottom = 10.dp)
            )
        }
    } else {
        BodyLargeTextWithCustomButton(
            buttonText = "Choose client",

        ) {
            onClick()
        }
    }

}