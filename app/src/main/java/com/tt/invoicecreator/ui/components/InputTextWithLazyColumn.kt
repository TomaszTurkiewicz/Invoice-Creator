package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.room.util.TableInfo
import com.tt.invoicecreator.data.roomV2.entities.ClientV2
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import java.util.Locale

@Composable
fun InputTextWithLazyColumn(
    labelText:String,
    inputText:String?,
    onValueChange: (String) -> Unit,
    listOfClients: List<ClientV2>,
    chooseClient: (ClientV2) -> Unit
) {

    val newList = if(inputText!="")
        listOfClients.filter {
            it.clientName.lowercase().contains(inputText!!)
        }
    else{
        listOfClients
    }

    val height = if(newList.size>5) 200.dp else (newList.size * 40).dp


    Column(

    ) {
        InputTextWithLabel(
            labelText = labelText,
            inputText = inputText,
            onValueChange = {
                onValueChange(it)
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
        ) {
            items(
                items = newList
            ){
                client ->
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .clickable(
                            onClick = {
                                chooseClient(client)
                            }
                        ),
                    textAlign = TextAlign.Center,
                    text = client.clientName
                )
            }
        }




    }
}