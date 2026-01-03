package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tt.invoicecreator.data.roomV2.entities.ItemV2

@Composable
fun ListOfItemsV2(
    list:List<ItemV2>,
    itemChosen: (ItemV2) -> Unit,
    onEditClicked: (ItemV2) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(
            items = list
        ){ item ->
            SingleRowItemV2(
                item = item,
                itemChosen = {
                    itemChosen(it)
                },
                onEditClicked = {
                    onEditClicked(it)
                }
            )

        }
    }
}