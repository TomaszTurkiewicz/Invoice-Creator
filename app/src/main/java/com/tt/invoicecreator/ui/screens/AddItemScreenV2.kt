package com.tt.invoicecreator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tt.invoicecreator.InvoiceCreatorScreen
import com.tt.invoicecreator.R
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.data.SharedPreferences
import com.tt.invoicecreator.data.roomV2.entities.ItemV2
import com.tt.invoicecreator.helpers.Currency
import com.tt.invoicecreator.helpers.DecimalFormatter
import com.tt.invoicecreator.ui.alert_dialogs.AlertDialogCurrencyChooserBlocked
import com.tt.invoicecreator.ui.components.CustomButton
import com.tt.invoicecreator.ui.components.InputDigitsWithLabel
import com.tt.invoicecreator.ui.components.InputTextWithLabel
import com.tt.invoicecreator.ui.components.texts.BodyLargeText
import com.tt.invoicecreator.ui.components.texts.BodyLargeTextWithFrame
import com.tt.invoicecreator.viewmodel.AppViewModel

@Composable
fun AddItemScreenV2(
    viewModel: AppViewModel,
    ignoredOnComposing: (AppBarState) -> Unit,
    navController: NavController,
    modePro: Boolean
) {

    val context = LocalContext.current
    val currency = remember {
        if(viewModel.getInvoiceItemList().isEmpty()){
            mutableStateOf(SharedPreferences.readCurrency(context))
        }else{
            mutableStateOf(viewModel.getInvoiceItemList()[0].itemV2.itemCurrency)
        }

    }

    val alertDialog = remember {
        mutableStateOf(false)
    }

    val chooser = viewModel.getInvoiceItemList().isEmpty()
    LaunchedEffect(key1 = true) {
        ignoredOnComposing(
            AppBarState(
                pro = modePro,
                title = "ADD NEW ITEM",
                action = {
                    Row {
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
            modifier = Modifier
                .fillMaxWidth(),
            labelText = "ITEM VALUE",
            inputText = itemValue.value,
            leadingIcon = if (currency.value.prefix) {
                { BodyLargeText(currency.value.symbol) }
            } else null,
            trailingIcon = if (!currency.value.prefix) {
                { BodyLargeText(currency.value.symbol) }
            } else null
        ) {
            itemValue.value = decimalFormatter.cleanup(it)
        }



        // --- Currency Chooser ---
        CurrencyChooser(
            selectedCurrency = currency.value,
            onCurrencySelected = {
                currency.value = it
                SharedPreferences.saveCurrency(context, it.name)
            },
            chooser = chooser,
            alertDialog = alertDialog
        )
        // ------------------------

        CustomButton(
            enabled = itemName.value.trim().isNotEmpty() && itemValue.value.isNotEmpty() && itemValue.value.toDouble() != 0.0,
            onClick ={
                viewModel.saveItemV2(
                    ItemV2(
                        itemName = itemName.value.trim(),
                        itemValue = itemValue.value.toDouble(),
                        itemCurrency = currency.value
                    )
                )
                navController.navigateUp()
            },
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            text = "SAVE"
        )
    }

    if(alertDialog.value){
        AlertDialogCurrencyChooserBlocked(
            invoiceItemV2 = viewModel.getInvoiceItemList()[0]
        ) {
            alertDialog.value = false
        }
    }

}

@Composable
fun CurrencyChooser(
    selectedCurrency: Currency,
    onCurrencySelected: (Currency) -> Unit,
    chooser:Boolean = true,
    alertDialog: MutableState<Boolean>
) {
    var expanded by remember { mutableStateOf(false) }
    val icon = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        // This Row mimics the appearance of an OutlinedTextField
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if(chooser){
                        expanded = true
                    }else{
                        alertDialog.value = true
                    }
                }
                .clip(RoundedCornerShape(4.dp)) // Matches OutlinedTextField corner radius
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 16.dp, vertical = 16.dp), // Matches OutlinedTextField padding
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BodyLargeText(
                text = "${selectedCurrency.name} (${selectedCurrency.symbol})"
            )
            if(chooser) {
                Icon(icon, contentDescription = "Select Currency")
            }
            }

        // The DropdownMenu remains the same but is now anchored to the Box
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.95f)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Get all currencies
            val currencies = Currency.entries

            // Process currencies in chunks of 2 for a two-column layout
            currencies.chunked(2).forEach { rowCurrencies ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Create an item for each currency in the pair
                    rowCurrencies.forEach { currency ->
                        Box(modifier = Modifier.weight(1f)) {
                            DropdownMenuItem(
                                text = {
                                    BodyLargeTextWithFrame(text = "${currency.name} - ${currency.symbol}")
                                       },
                                onClick = {
                                    onCurrencySelected(currency)
                                    expanded = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    // If there's only one item in the last row, add an empty Box to maintain alignment
                    if (rowCurrencies.size == 1) {
                        Box(modifier = Modifier.weight(1f)) {}
                    }
                }
            }
        }
    }
}