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
import com.tt.invoicecreator.data.roomV2.entities.InvoiceItemV2
import com.tt.invoicecreator.data.roomV2.entities.ItemV2
import com.tt.invoicecreator.ui.alert_dialogs.AlertDialogItemCountDiscountAndCommentsV2
import com.tt.invoicecreator.ui.alert_dialogs.AlertDialogItemWithCurrency
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
    val itemListInUse = itemList?.filter {
        it.inUse
    }

    val alertDialog = remember {
        mutableStateOf(false)
    }

    val editAlertDialog = remember {
        mutableStateOf(false)
    }

    val alertDialogAddItemToDatabase = remember {
        mutableStateOf(false)
    }

    val itemV2Temp = remember {
        mutableStateOf(ItemV2())
    }

    val editItem = remember {
        mutableStateOf(ItemV2())
    }

    val tempItemList = if(viewModel.getInvoiceItemList().isEmpty()){
        itemListInUse
    }else{
        itemListInUse?.filter { itemV2 ->
            itemV2.itemCurrency.symbol == viewModel.getInvoiceItemList()[0].itemV2.itemCurrency.symbol
        }
    }

    LaunchedEffect(key1 = true) {
        ignoredOnComposing(
            AppBarState(
                title = "CHOOSE ITEM",
                action = {
                    Row {
                        IconButton(onClick = {
                            alertDialogAddItemToDatabase.value = true
//                            navController.navigate(InvoiceCreatorScreen.AddItemV2.name)
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
        if(tempItemList.isNullOrEmpty()){
            TitleLargeText(
                text = "press + on top of the app"
            )
        }
        else{
            ListOfItemsV2(
                list = tempItemList,
                itemChosen = {
                    itemV2Temp.value = it
//                    viewModel.addItemToInvoice(it)
                    alertDialog.value = true
                },
                onEditClicked = {
                    editItem.value = it
                    editAlertDialog.value = true
                }
            )
        }
    }

    if(alertDialog.value){
        AlertDialogItemCountDiscountAndCommentsV2(
            title = "ADD ITEM",
            itemV2 = itemV2Temp.value,
            onDismissRequest = {
                alertDialog.value = false
            },
            onButtonClicked = { itemV2, itemCount, itemDiscount, itemComment, isVat, vat ->
                viewModel.addItemToInvoice(
                    InvoiceItemV2(
                        itemV2 = itemV2,
                        itemCount = itemCount.toDouble(),
                        itemDiscount = itemDiscount.toDouble(),
                        comment = itemComment,
                        vat = if(isVat){
                            vat.toDoubleOrNull()
                        }else{
                            null
                        }
                    )
                    )
                navController.navigateUp()
            }
        )
    }

    if(alertDialogAddItemToDatabase.value){
        AlertDialogItemWithCurrency(
            viewModel = viewModel,
            title = "ADD ITEM",
            buttonTextOne = "SAVE",
            firstButtonAction = { itemName, itemValue, currency ->
                viewModel.saveItemV2(
                    ItemV2(
                        itemName = itemName,
                        itemValue = itemValue,
                        itemCurrency = currency
                    )
                )
            },
            onDismissRequest = {
                alertDialogAddItemToDatabase.value = false
            }
        )
    }

    if(editAlertDialog.value){
        AlertDialogItemWithCurrency(
            viewModel = viewModel,
            title = "EDIT ITEM",
            itemName = editItem.value.itemName,
            itemValue = editItem.value.itemValue.toString(),
            buttonTextOne = "UPDATE",
            buttonTextTwo = "DELETE",
            secondButtonEnabled = true,
            firstButtonAction = { itemName, itemValue, currency ->
                viewModel.updateItem(editItem.value.copy(inUse = false))
                viewModel.saveItemV2(
                    ItemV2(
                        itemName = itemName,
                        itemValue = itemValue,
                        itemCurrency = currency
                    )
                )
            },
            secondButtonAction = {
                viewModel.updateItem(editItem.value.copy(inUse = false))
            },
            onDismissRequest = {
                editAlertDialog.value = false
            }
        )
    }

}