package com.tt.invoicecreator.ui.alert_dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.roomV2.entities.ItemV2
import com.tt.invoicecreator.helpers.DecimalFormatter
import com.tt.invoicecreator.ui.components.CustomButton
import com.tt.invoicecreator.ui.components.InputDigitsWithLabel
import com.tt.invoicecreator.ui.components.InputTextWithLabel
import com.tt.invoicecreator.ui.components.cards.CustomCardView
import com.tt.invoicecreator.ui.components.texts.BodyLargeText
import com.tt.invoicecreator.ui.components.texts.TitleLargeText
import com.tt.invoicecreator.ui.theme.myColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogItemCountDiscountAndCommentsV2(
    title:String,
    itemV2: ItemV2,
    itemCount:String = "1",
    itemDiscount:String = "0",
    itemComment:String = "",
    initialVat:String = "20",
    isVat:Boolean = false,
    deleteButton: Boolean = false,
    onDismissRequest: () -> Unit,
    onButtonClicked:(item:ItemV2, count:String, discount:String, comment:String, isVat:Boolean, vat:String) -> Unit,
    onDeleteClicked:() -> Unit = {}
) {
    val itemCount = remember {
        mutableStateOf(itemCount)
    }

    val itemDiscount = remember {
        mutableStateOf(itemDiscount)
    }

    val itemComment = remember {
        mutableStateOf(itemComment)
    }

    val initialVAT = remember {
        mutableStateOf(initialVat)
    }
    val decimalFormatter = DecimalFormatter()
    val isVat = remember {
        mutableStateOf(isVat)
    }

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
                    text = title
                )

                BodyLargeText(
                    text = itemV2.itemName,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
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
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    BodyLargeText(
                        text = "ADD VAT?",
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 20.dp)
                    )
                    Switch(
                        checked = isVat.value,
                        onCheckedChange = {
                            isVat.value = it
                        },
                        modifier = Modifier
                            .padding(end = 20.dp),
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.myColors.primaryDark,
                            checkedTrackColor = MaterialTheme.myColors.primaryLight,
                            uncheckedThumbColor = MaterialTheme.myColors.material.outline,
                            uncheckedTrackColor = MaterialTheme.myColors.material.surfaceVariant
                        )
                    )

                }

                if(isVat.value) {
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
                            isError = if (initialVAT.value == "") {
                                true
                            } else {
                                initialVAT.value.toDouble() >= 100
                            },
                            errorText = if (initialVAT.value == "") {
                                "not valid amount"
                            } else {
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
                }


                CustomButton(
                    enabled = if(isVat.value)
                        itemCount.value.isNotEmpty() && itemDiscount.value.isNotEmpty() && initialVAT.value!="" && initialVAT.value.toDouble()<100
                    else
                        itemCount.value.isNotEmpty() && itemDiscount.value.isNotEmpty(),
                    onClick = {

                        onButtonClicked(
                            itemV2,
                            itemCount.value,
                            itemDiscount.value,
                            itemComment.value.trim(),
                            isVat.value,
                            initialVAT.value
                        )

                        onDismissRequest()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    text = "SAVE"
                )

                if(deleteButton){
                    CustomButton(
                        enabled = true,
                        onClick = {
                            onDeleteClicked()
                            onDismissRequest()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 30.dp,
                                bottom = 5.dp,
                                start = 5.dp,
                                end = 5.dp
                            ),
                        text = "DELETE ITEM",
                        makeItWarning = true
                    )
                }

            }
        }

    }
}