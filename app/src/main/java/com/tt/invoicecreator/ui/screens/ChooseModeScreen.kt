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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.tt.invoicecreator.InvoiceCreatorScreen
import com.tt.invoicecreator.data.SharedPreferences

@Composable
fun ChooseModeScreen(
    navController: NavController
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        /** working one item invoice **/
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.33f),
            contentAlignment = Alignment.BottomCenter
        ){
            Button(
                onClick = {
                    navController.navigate(InvoiceCreatorScreen.Invoices.name)
                }
            ) {
                Text(text = "WORKING 1 ITEM")
            }
        }

        /** one item invoice version 2.0 **/
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            contentAlignment = Alignment.Center
        ){
            Button(
                onClick = {
                    SharedPreferences.saveOneItemMode(context = context, oneItemMode = true)
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
            contentAlignment = Alignment.TopCenter
        ){
            Button(
                onClick = {
                    SharedPreferences.saveOneItemMode(context = context, oneItemMode = false)
                    navController.navigate(InvoiceCreatorScreen.InvoicesV2.name)
                }
            ) {
                Text(text = "MANY ITEMS V2.0")
            }
        }
    }
}