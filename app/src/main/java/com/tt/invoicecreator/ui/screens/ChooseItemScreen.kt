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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.tt.invoicecreator.InvoiceCreatorScreen
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.ui.alert_dialogs.AlertDialogItemCountDiscountAndComments
import com.tt.invoicecreator.ui.components.ListOfItems
import com.tt.invoicecreator.viewmodel.AppViewModel

@Composable
fun ChooseItemScreen(
    viewModel: AppViewModel,
    ignoredOnComposing: (AppBarState) -> Unit,
    navController: NavController
) {

    val itemList by viewModel.itemList.observeAsState()

    val alertDialog = remember {
        mutableStateOf(false)
    }



    LaunchedEffect(key1 = true) {
        ignoredOnComposing(
            AppBarState(
                action = {
                    Row {
                        IconButton(onClick = {
                            navController.navigate(InvoiceCreatorScreen.AddItem.name)
                        }) {
                            Icon(Icons.Default.Add, null)
                        }
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

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center)
    {
        if(itemList.isNullOrEmpty()){
            Text(
                text = "press + on top of the app"
            )
        }
        else{
            ListOfItems(
                list = itemList!!,
                itemChosen = {
                    viewModel.getInvoice().item = it
                    alertDialog.value = true
                }
            )
        }
    }

    if(alertDialog.value){
        AlertDialogItemCountDiscountAndComments(
            viewModel = viewModel,
            onDismissRequest = {
                alertDialog.value = false
            },
            navController = navController
        )
    }

}