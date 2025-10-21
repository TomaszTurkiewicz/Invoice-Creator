package com.tt.invoicecreator.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.tt.invoicecreator.InvoiceCreatorScreen
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.data.roomV2.entities.ClientV2
import com.tt.invoicecreator.ui.components.ListOfClientsV2
import com.tt.invoicecreator.viewmodel.AppViewModel

@Composable
fun ChooseClientScreenV2(
    viewModel: AppViewModel,
    ignoredOnComposing: (AppBarState) -> Unit,
    navController: NavController,
    clientList:  List<ClientV2>?
) {

    LaunchedEffect(key1 = true) {
        ignoredOnComposing(
            AppBarState(
                title = "CHOOSE CLIENT",
                action = {
                    Row {
                        IconButton(onClick = {
                            navController.navigate(InvoiceCreatorScreen.AddClientV2.name)
                        }) {
                            Icon(Icons.Default.Add, null)
                        }
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

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center)
    {
        if(clientList.isNullOrEmpty()){
            Text(
                text = "press + on top of the app"
            )
        }
        else{
            ListOfClientsV2(
                list = clientList!!,
                clientChosen = {
                    viewModel.getInvoiceV2().client = it
                    navController.navigateUp()
                }
            )
        }
    }
}