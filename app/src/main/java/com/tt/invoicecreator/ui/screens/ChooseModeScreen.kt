package com.tt.invoicecreator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.tt.invoicecreator.InvoiceCreatorScreen
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.viewmodel.AppViewModel

@Composable
fun ChooseModeScreen(
    viewModel: AppViewModel,
    ignoredOnComposing: (AppBarState) -> Unit,
    navController: NavController
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        ignoredOnComposing(
            AppBarState(
                pro = false,
                title = "CHOOSE MODE",
                action = null
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        /** one item invoice version 2.0 **/
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            contentAlignment = Alignment.Center
        ){
            Button(
                onClick = {
                    viewModel.setModePro(context,false)
                    navController.navigate(InvoiceCreatorScreen.InvoicesV2.name)
                }
            ) {
                Text(text = "1 ITEM V2.0")
            }
        }

        /** multi items invoice version 2.0 **/
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ){
            Button(
                onClick = {
                    viewModel.setModePro(context,true)
                    navController.navigate(InvoiceCreatorScreen.InvoicesV2.name)
                }
            ) {
                Text(text = "MANY ITEMS V2.0")
            }
        }
    }
}