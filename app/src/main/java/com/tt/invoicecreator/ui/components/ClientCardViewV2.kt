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
    clientName: String,
    clientAddressLine1: String,
    clientAddressLine2: String,
    clientCity: String,
    showCardView: () -> Boolean,
    onClick: () -> Unit
) {
    CustomCardView(
        modifier = Modifier
            .clickable {
                onClick()
            }
    ){
//        if(viewModel.getInvoiceV2().client.clientName != ""){
        if(showCardView()){
            Column() {
                Text(
                    text = clientName,
                    fontWeight = FontWeight.W700,
                    modifier = Modifier
                        .padding(5.dp))
                Text(
                    text = clientAddressLine1,
                    fontWeight = FontWeight.W300,
                    modifier = Modifier
                        .padding(5.dp))
                Text(
                    text = clientAddressLine2,
                    fontWeight = FontWeight.W300,
                    modifier = Modifier
                        .padding(5.dp))
                Text(
                    text = clientCity,
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
