package com.tt.invoicecreator.ui.alert_dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.SharedPreferences
import com.tt.invoicecreator.helpers.Currency
import com.tt.invoicecreator.helpers.DecimalFormatter
import com.tt.invoicecreator.ui.components.CurrencyChooser
import com.tt.invoicecreator.ui.components.CustomButton
import com.tt.invoicecreator.ui.components.InputDigitsWithLabel
import com.tt.invoicecreator.ui.components.InputTextWithLabel
import com.tt.invoicecreator.ui.components.cards.CustomCardView
import com.tt.invoicecreator.ui.components.texts.BodyLargeText
import com.tt.invoicecreator.ui.components.texts.TitleLargeText
import com.tt.invoicecreator.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogItemWithCurrency(
    viewModel: AppViewModel,
    title:String,
    itemName:String = "",
    itemValue:String = "",
    buttonTextOne: String,
    buttonTextTwo: String = "",
    secondButtonEnabled: Boolean = false,
    firstButtonAction: (firstLine:String, secondLine: Double, currency: Currency) -> Unit,
    secondButtonAction: () -> Unit = {},
    onDismissRequest: () -> Unit
)
{
    val context = LocalContext.current

    val chooser = viewModel.getInvoiceItemList().isEmpty()

    val alertDialog = remember {
        mutableStateOf(false)
    }

    val currency = remember {
        if(viewModel.getInvoiceItemList().isEmpty()){
            mutableStateOf(SharedPreferences.readCurrency(context))
        }else{
            mutableStateOf(viewModel.getInvoiceItemList()[0].itemV2.itemCurrency)
        }

    }

    val itemNameMut = remember {
        mutableStateOf(itemName)
    }

    val itemValueMut = remember {
        mutableStateOf(itemValue)
    }

    val decimalFormatter = DecimalFormatter()

    BasicAlertDialog(
        onDismissRequest = {
            onDismissRequest()
        }
    ){
        CustomCardView(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column() {
                TitleLargeText(
                    text = title,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                InputTextWithLabel(
                    labelText = "ITEM NAME",
                    inputText = itemNameMut.value
                ) {
                    itemNameMut.value = it
                }

                InputDigitsWithLabel(
                    modifier = Modifier
                        .fillMaxWidth(),
                    labelText = "ITEM VALUE",
                    inputText = itemValueMut.value,
                    leadingIcon = if (currency.value.prefix) {
                        { BodyLargeText(currency.value.symbol) }
                    } else null,
                    trailingIcon = if (!currency.value.prefix) {
                        { BodyLargeText(currency.value.symbol) }
                    } else null
                ) {
                    itemValueMut.value = decimalFormatter.cleanup(it)
                }

                CurrencyChooser(
                    selectedCurrency = currency.value,
                    onCurrencySelected = {
                        currency.value = it
                        SharedPreferences.saveCurrency(context, it.name)
                    },
                    chooser = chooser,
                    alertDialog = alertDialog
                )

                CustomButton(
                    enabled = itemNameMut.value.trim().isNotEmpty() && itemValueMut.value.isNotEmpty() && itemValueMut.value.toDouble() != 0.0,
                    onClick ={
                        firstButtonAction(itemNameMut.value.trim(), itemValueMut.value.toDouble(), currency.value)

//                        viewModel.saveItemV2(
//                            ItemV2(
//                                itemName = itemNameMut.value.trim(),
//                                itemValue = itemValueMut.value.toDouble(),
//                                itemCurrency = currency.value
//                            )
//                        )
                        onDismissRequest()
                    },
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                    text = buttonTextOne
                )

                if(secondButtonEnabled){
                    CustomButton(
                        enabled = itemNameMut.value.trim().isNotEmpty() && itemValueMut.value.isNotEmpty() && itemValueMut.value.toDouble() != 0.0,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                bottom = 5.dp,
                                start = 5.dp,
                                end = 5.dp,
                                top = 30.dp),
                        text = buttonTextTwo,
                        makeItWarning = true,
                        onClick = {
                            secondButtonAction()
                            onDismissRequest()
                        }
                    )
                }


            }
        }
    }

    if(alertDialog.value){
        AlertDialogCurrencyChooserBlocked(
            invoiceItemV2 = viewModel.getInvoiceItemList()[0]
        ) {
            alertDialog.value = false
        }
    }
}