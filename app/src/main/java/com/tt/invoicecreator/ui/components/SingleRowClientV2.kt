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
import com.tt.invoicecreator.data.roomV2.entities.ClientV2

@Composable
fun SingleRowClientV2(
    client: ClientV2,
    clientChosen: (ClientV2) -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                clientChosen(client)
            },
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ){
        Column(
            modifier = Modifier
                .padding(24.dp)
        ) {
            Text(
                text = client.clientName,
                fontWeight = FontWeight.W700,
                modifier = Modifier
                    .padding(5.dp)
            )
            Text(
                text = client.clientAddress1,
                fontWeight = FontWeight.W500,
                modifier = Modifier
                    .padding(5.dp)
            )
            Text(
                text = client.clientAddress2,
                fontWeight = FontWeight.W500,
                modifier = Modifier
                    .padding(5.dp)
            )
            Text(
                text = client.clientCity,
                fontWeight = FontWeight.W500,
                modifier = Modifier
                    .padding(5.dp)
            )
        }
    }
}