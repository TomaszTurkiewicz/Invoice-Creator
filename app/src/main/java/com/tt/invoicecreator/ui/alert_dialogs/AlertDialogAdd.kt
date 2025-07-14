package com.tt.invoicecreator.ui.alert_dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogAdd(
    onCanceledClicked: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = {
            onCanceledClicked()
        },

    ) {
        Box(
            modifier = Modifier
                .background(Color.LightGray)
                .padding(10.dp)
        ){
            Column {
                Text(
                    text = "ADD...",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    textAlign = TextAlign.Center)

                Button(
                    onClick = {
                        //todo
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)

                ) {
                    Text("ITEM")
                }

                Button(
                    onClick = {
                        //todo
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)

                ) {
                    Text("CLIENT")
                }

                Button(
                    onClick = {
                        //todo
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)

                ) {
                    Text("INVOICE")
                }
            }

        }
    }
}

