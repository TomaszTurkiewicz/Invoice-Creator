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
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.ui.alert_dialogs.AlertDialogAdd
import com.tt.invoicecreator.ui.components.ListOfInvoices
import com.tt.invoicecreator.viewmodel.AppViewModel

@Composable
fun InvoicesScreen (
    viewModel: AppViewModel,
    ignoredOnComposing: (AppBarState) -> Unit
){
    val invoicesList by viewModel.invoiceList.observeAsState()

    val alertDialogAdd = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = true) {
        ignoredOnComposing(
            AppBarState(
                action = {
                    Row {
                        IconButton(onClick = {
                            alertDialogAdd.value = true
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

    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center){
        if(invoicesList.isNullOrEmpty()){
            Text(
                text = "press + on top of the app",
            )
        }
        else{
            ListOfInvoices()
        }
    }

    if(alertDialogAdd.value){
        AlertDialogAdd {
            alertDialogAdd.value = false
        }
    }

}