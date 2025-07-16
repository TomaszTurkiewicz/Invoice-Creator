package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tt.invoicecreator.data.room.Client

@Composable
fun ListOfClients(
    list:List<Client>,
    clientChosen: (Client) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(
            items = list
        ){
            client ->
            SingleRowClient(
                client = client,
                clientChosen = {
                    clientChosen(it)
                }
            )
        }
    }
}