package com.tt.invoicecreator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tt.invoicecreator.InvoiceCreatorScreen
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.ui.components.ClientCardView
import com.tt.invoicecreator.ui.components.InputTextWithLabel
import com.tt.invoicecreator.ui.components.InvoiceNumberCardView
import com.tt.invoicecreator.ui.components.ItemCardView
import com.tt.invoicecreator.ui.components.PaymentMethodCardView
import com.tt.invoicecreator.viewmodel.AppViewModel

@Composable
fun AddInvoiceScreen(
    viewModel: AppViewModel,
    ignoredOnComposing: (AppBarState) -> Unit,
    navController: NavController
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
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        InvoiceNumberCardView()

        ClientCardView(
            onClick = {
                navController.navigate(InvoiceCreatorScreen.ChooseClient.name)
            }
        )

        ItemCardView(
            viewModel = viewModel,
            onClick = {
                navController.navigate(InvoiceCreatorScreen.ChooseItem.name)
            }
        )

        PaymentMethodCardView()

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
}