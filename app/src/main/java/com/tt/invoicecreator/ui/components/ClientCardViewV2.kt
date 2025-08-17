package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.viewmodel.AppViewModel

@Composable
fun ClientCardViewV2(
    viewModel: AppViewModel,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                onClick()
            },
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ){
        if(viewModel.getInvoiceV2().client.clientName != ""){
            Column(
                modifier = Modifier
                    .padding(24.dp)
            ) {
                Text(
                    text = viewModel.getInvoiceV2().client.clientName,
                    fontWeight = FontWeight.W700,
                    modifier = Modifier
                        .padding(5.dp))
                Text(
                    text = viewModel.getInvoiceV2().client.clientAddress1,
                    fontWeight = FontWeight.W300,
                    modifier = Modifier
                        .padding(5.dp))
                Text(
                    text = viewModel.getInvoiceV2().client.clientAddress2,
                    fontWeight = FontWeight.W300,
                    modifier = Modifier
                        .padding(5.dp))
                Text(
                    text = viewModel.getInvoiceV2().client.clientCity,
                    fontWeight = FontWeight.W300,
                    modifier = Modifier
                        .padding(5.dp))
            }
        }
        else{
            Text(
                text = "Client not chosen yet",
                fontWeight = FontWeight.W700,
                modifier = Modifier
                    .padding(5.dp))
        }
    }
}
