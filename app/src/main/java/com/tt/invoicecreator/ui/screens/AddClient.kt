package com.tt.invoicecreator.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.ui.components.InputTextWithLabel

@Composable
fun AddClient(
    ignoredOnComposing: (AppBarState) -> Unit
) {
    LaunchedEffect(key1 = true) {
        ignoredOnComposing(
            AppBarState(
                action = {
                    Row {
                        IconButton(onClick = {
                            //todo
                        }) {
                            Icon(Icons.Default.Settings,null)
                        }
                    }

                }
            )
        )
    }

    Column {
        InputTextWithLabel(
            labelText = "Client name",
            inputText = "client name"
        ) {
            //todo
        }
        InputTextWithLabel(
            labelText = "Client address line 1",
            inputText = "client address 1"
        ) {
            //todo
        }
        InputTextWithLabel(
            labelText = "Client address line 2",
            inputText = "client address 2"
        ) {
            //todo
        }
        InputTextWithLabel(
            labelText = "Client city",
            inputText = "client city"
        ) {
            //todo
        }
        Button(
            onClick ={
                //todo
            },
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
        ) {
            Text(text = "SAVE")
        }
    }

    Column {

    }
}
