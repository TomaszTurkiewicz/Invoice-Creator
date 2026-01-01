package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.roomV2.entities.ClientV2
import com.tt.invoicecreator.ui.components.cards.CustomCardView
import com.tt.invoicecreator.ui.components.texts.BodyLargeText

@Composable
fun SingleRowClientV2(
    client: ClientV2,
    clientChosen: (ClientV2) -> Unit,
    editClient: (ClientV2) -> Unit
) {
    CustomCardView(
        modifier = Modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column(
                modifier = Modifier
                    .clickable{
                        clientChosen(client)
                    }
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

            CustomIconButton(
                onClick = {
                    editClient(client)
                },
                imageVector = Icons.Default.Edit
            )
        }




    }
}