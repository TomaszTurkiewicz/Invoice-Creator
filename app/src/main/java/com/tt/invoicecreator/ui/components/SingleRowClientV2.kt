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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.roomV2.entities.ClientV2
import com.tt.invoicecreator.ui.components.texts.BodyLargeText

@Composable
fun SingleRowClientV2(
    client: ClientV2,
    clientChosen: (ClientV2) -> Unit
) {
    CustomCardView(
        modifier = Modifier
            .clickable {
                clientChosen(client)
            }
    ){
        Column(
            modifier = Modifier
        ) {
            BodyLargeText(
                text = client.clientName,
                modifier = Modifier
                    .padding(5.dp)
            )
            BodyLargeText(
                text = client.clientAddress1,
                modifier = Modifier
                    .padding(5.dp)
            )
            BodyLargeText(
                text = client.clientAddress2,
                modifier = Modifier
                    .padding(5.dp)
            )
            BodyLargeText(
                text = client.clientCity,
                modifier = Modifier
                    .padding(5.dp)
            )
        }
    }
}