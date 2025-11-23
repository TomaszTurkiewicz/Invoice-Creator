package com.tt.invoicecreator.ui.alert_dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tt.invoicecreator.data.roomV2.entities.InvoiceItemV2
import com.tt.invoicecreator.data.roomV2.entities.ItemV2
import com.tt.invoicecreator.helpers.DecimalFormatter
import com.tt.invoicecreator.ui.components.CustomButton
import com.tt.invoicecreator.ui.components.InputDigitsWithLabel
import com.tt.invoicecreator.ui.components.InputTextWithLabel
import com.tt.invoicecreator.ui.components.cards.CustomCardView
import com.tt.invoicecreator.ui.components.texts.BodyLargeText
import com.tt.invoicecreator.ui.components.texts.TitleLargeText
import com.tt.invoicecreator.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogItemCountDiscountAndCommentsV2(
    itemV2: ItemV2,
    viewModel: AppViewModel?,
    onDismissRequest: () -> Unit,
    navController: NavController
) {
    val itemCount = remember {
        mutableStateOf("1")
    }

    val itemDiscount = remember {
        mutableStateOf("0")
    }

    val itemComment = remember {
        mutableStateOf("")
    }

    val initialVAT = remember {
        mutableStateOf("20")
    }
    val decimalFormatter = DecimalFormatter()

    BasicAlertDialog(
        onDismissRequest = {
            onDismissRequest()
        }
    ) {
        CustomCardView {
            Column(
                modifier = Modifier

            )
            {
                TitleLargeText(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    text = "ADD ITEM"
                )
                InputDigitsWithLabel(
                    modifier = Modifier
                        .fillMaxWidth(),
                    labelText = "QUANTITY",
                    inputText = itemCount.value
                ) {
                    itemCount.value = decimalFormatter.cleanup(it)
                }
                InputDigitsWithLabel(
                    modifier = Modifier
                        .fillMaxWidth(),
                    labelText = "DISCOUNT",
                    inputText = itemDiscount.value,
                    leadingIcon = if(itemV2.itemCurrency.prefix) {
                        {  BodyLargeText(itemV2.itemCurrency.symbol)}
                    }
                    else{
                     null
                    },
                    trailingIcon = if(!itemV2.itemCurrency.prefix) {
                        { BodyLargeText(itemV2.itemCurrency.symbol)}}
                    else{
                        null
                    }
                ) {
                    itemDiscount.value = decimalFormatter.cleanup(it)
                }
                InputTextWithLabel(
                    labelText = "COMMENT",
                    inputText = itemComment.value
                ) {
                    itemComment.value = it
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)

                ) {
                    InputDigitsWithLabel(
                        modifier = Modifier
                            .fillMaxWidth(0.8f),
                        labelText = "VAT",
                        inputText = initialVAT.value,
                        isError = if(initialVAT.value==""){
                            true
                        }
                        else{
                            initialVAT.value.toDouble()>=100
                        },
                        errorText = if(initialVAT.value==""){
                            "not valid amount"
                        }
                        else{
                            "too much"
                        }
                    ) {
                        initialVAT.value = decimalFormatter.cleanup(it)
                    }
                    TitleLargeText(
                        text = "%",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    )
                }
                CustomButton(
                    enabled = itemCount.value.isNotEmpty() && itemDiscount.value.isNotEmpty() && initialVAT.value!="" && initialVAT.value.toDouble()<100,
                    onClick = {

                        viewModel?.addItemToInvoice(
                            InvoiceItemV2(
                                itemV2 = itemV2,
                                itemCount = itemCount.value.toDouble(),
                                itemDiscount = itemDiscount.value.toDouble(),
                                comment = itemComment.value.trim(),
                                vat = initialVAT.value.toDoubleOrNull()
                            )
                        )
                        navController.navigateUp()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    text = "SAVE"
                )

                CustomButton(
                    enabled = itemCount.value.isNotEmpty() && itemDiscount.value.isNotEmpty(),
                    onClick = {

                        viewModel?.addItemToInvoice(
                            InvoiceItemV2(
                                itemV2 = itemV2,
                                itemCount = itemCount.value.toDouble(),
                                itemDiscount = itemDiscount.value.toDouble(),
                                comment = itemComment.value.trim(),
                                vat = null
                            )
                        )
                        navController.navigateUp()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    text = "NO VAT"
                )

            }
        }

    }
}