package com.tt.invoicecreator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.tt.invoicecreator.InvoiceCreatorScreen
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.data.roomV2.entities.ClientV2
import com.tt.invoicecreator.data.roomV2.entities.InvoiceV2
import com.tt.invoicecreator.ui.components.DotShape
import com.tt.invoicecreator.ui.components.FilteredInvoicesCardView
import com.tt.invoicecreator.viewmodel.AppViewModel

@Composable
fun InvoicesByClientScreen(
    viewModel: AppViewModel,
    ignoredOnComposing: (AppBarState) -> Unit,
    navController: NavController,
    listOfClients: List<ClientV2>?,
    listOfInvoices: List<InvoiceV2>?
) {
    LaunchedEffect(key1 = true) {
        ignoredOnComposing(
            AppBarState(
                action = {
                    Row {
                        IconButton(onClick = {
                            viewModel.cleanInvoiceV2()
                            navController.navigate(InvoiceCreatorScreen.AddInvoiceV2.name)
                        }) {
                            Icon(Icons.Default.Add, null)
                        }

                        IconButton(onClick = {
                            navController.navigate(InvoiceCreatorScreen.Settings.name)
                        }) {
                            Icon(Icons.Default.Settings, null)
                        }
                    }
                }
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
           items(listOfClients!!)
            {
                FilteredInvoicesCardView(
                    header = it.clientName,
                    message = "Invoices: ",
                    count = listOfInvoices!!.filter { invoice ->
                        invoice.client.clientId == it.clientId
                    }.size,
                    onClick = {
                        if(listOfInvoices.any { invoice ->
                                invoice.client.clientId == it.clientId
                            }){
                            viewModel.chosenClientToFilterInvoices = it
                            navController.navigate(InvoiceCreatorScreen.FilteredInvoicesByClient.name)
                        }

                    }
                )
            }
        }
    }
}
