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
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.data.room.Item
import com.tt.invoicecreator.data.roomV2.ItemV2
import com.tt.invoicecreator.helpers.DecimalFormatter
import com.tt.invoicecreator.ui.components.InputDigitsWithLabel
import com.tt.invoicecreator.ui.components.InputTextWithLabel
import com.tt.invoicecreator.viewmodel.AppViewModel

@Composable
fun AddItemScreenV2(
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

    val itemName = remember {
        mutableStateOf("")
    }
    val itemValue = remember {
        mutableStateOf("")
    }
    val decimalFormatter = DecimalFormatter()

    Column {
        InputTextWithLabel(
            labelText = "ITEM NAME",
            inputText = itemName.value
        ) {
            itemName.value = it
        }


        InputDigitsWithLabel(
            labelText = "ITEM VALUE",
            inputText = itemValue.value
        ) {
            itemValue.value = decimalFormatter.cleanup(it)
        }
        Button(
            enabled = itemName.value.trim().isNotEmpty() && itemValue.value.isNotEmpty() && itemValue.value.toDouble() != 0.0,
            onClick ={
                viewModel.saveItemV2(
                    ItemV2(
                        itemName = itemName.value.trim(),
                        itemValue = itemValue.value.toDouble()
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