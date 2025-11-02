package com.tt.invoicecreator.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.tt.invoicecreator.InvoiceCreatorScreen
import com.tt.invoicecreator.R
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.data.roomV2.entities.ItemV2
import com.tt.invoicecreator.ui.alert_dialogs.AlertDialogItemCountDiscountAndCommentsV2
import com.tt.invoicecreator.ui.components.ListOfItemsV2
import com.tt.invoicecreator.ui.components.texts.TitleLargeText
import com.tt.invoicecreator.viewmodel.AppViewModel

@Composable
fun ChooseItemScreenV2(
    viewModel: AppViewModel,
    ignoredOnComposing: (AppBarState) -> Unit,
    navController: NavController,
    itemList: List<ItemV2>?
) {

    val alertDialog = remember {
        mutableStateOf(false)
    }

    val itemV2Temp = remember {
        mutableStateOf(ItemV2())
    }

    LaunchedEffect(key1 = true) {
        ignoredOnComposing(
            AppBarState(
                title = "CHOOSE ITEM",
                action = {
                    Row {
                        IconButton(onClick = {
                            navController.navigate(InvoiceCreatorScreen.AddItemV2.name)
                        }) {
                            Icon(painter = painterResource(R.drawable.baseline_add_24), null)
                        }
                        IconButton(onClick = {
                            navController.navigate(InvoiceCreatorScreen.Settings.name)
                        }) {
                            Icon(painter = painterResource(R.drawable.baseline_settings_24), null)
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
            TitleLargeText(
                text = "press + on top of the app"
            )
        }
        else{
            ListOfItemsV2(
                list = itemList!!,
                itemChosen = {
                    itemV2Temp.value = it
//                    viewModel.addItemToInvoice(it)
                    alertDialog.value = true
                }
            )
        }
    }

    if(alertDialog.value){
        AlertDialogItemCountDiscountAndCommentsV2(
            itemV2 = itemV2Temp.value,
            viewModel = viewModel,
            onDismissRequest = {
                alertDialog.value = false
            },
            navController = navController
        )
    }

}