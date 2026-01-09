package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tt.invoicecreator.data.roomV2.entities.ClientV2

@Composable
fun ListOfClientsV2(
    list:List<ClientV2>?,
    clientChosen: (ClientV2) -> Unit,
    editClient: (ClientV2) -> Unit
) {
    if (list == null) return
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(
            items = list
        ){
                client ->
            SingleRowClientV2(
                client = client,
                clientChosen = {
                    clientChosen(it)
                },
                editClient = {
                    editClient(it)
                }
            )
        }
    }
}