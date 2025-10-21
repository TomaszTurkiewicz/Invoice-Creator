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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tt.invoicecreator.InvoiceCreatorScreen
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.data.roomV2.entities.ClientV2
import com.tt.invoicecreator.ui.components.InputTextWithLabel
import com.tt.invoicecreator.viewmodel.AppViewModel

@Composable
fun AddClientScreenV2(
    viewModel: AppViewModel,
    ignoredOnComposing: (AppBarState) -> Unit,
    navController: NavController
) {
    val clientName = remember {
        mutableStateOf("")
    }
    val clientAddress1 = remember {
        mutableStateOf("")
    }
    val clientAddress2 = remember {
        mutableStateOf("")
    }
    val clientCity = remember {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = true) {
        ignoredOnComposing(
            AppBarState(
                title = "ADD NEW CLIENT",
                action = {
                    Row {
                        IconButton(onClick = {
                            navController.navigate(InvoiceCreatorScreen.Settings.name)
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
            inputText = clientName.value
        ) {
            clientName.value = it
        }
        InputTextWithLabel(
            labelText = "Client address line 1",
            inputText = clientAddress1.value
        ) {
            clientAddress1.value = it
        }
        InputTextWithLabel(
            labelText = "Client address line 2",
            inputText = clientAddress2.value
        ) {
            clientAddress2.value = it
        }
        InputTextWithLabel(
            labelText = "Client city",
            inputText = clientCity.value
        ) {
            clientCity.value = it
        }
        Button(
            enabled = clientName.value.trim().isNotEmpty()
                    && clientAddress1.value.trim().isNotEmpty()
                    && clientAddress2.value.trim().isNotEmpty()
                    && clientCity.value.trim().isNotEmpty(),
            onClick ={
                viewModel.saveClientV2(
                    ClientV2(
                        clientName = clientName.value.trim(),
                        clientAddress1 = clientAddress1.value.trim(),
                        clientAddress2 = clientAddress2.value.trim(),
                        clientCity = clientCity.value.trim()
                    )
                )
                navController.navigateUp()
            },
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
        ) {
            Text(text = "SAVE")
        }
    }

}