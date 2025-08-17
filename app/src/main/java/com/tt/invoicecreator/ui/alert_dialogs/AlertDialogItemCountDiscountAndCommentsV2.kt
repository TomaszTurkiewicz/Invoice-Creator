package com.tt.invoicecreator.ui.alert_dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tt.invoicecreator.data.roomV2.InvoiceItemV2
import com.tt.invoicecreator.data.roomV2.ItemV2
import com.tt.invoicecreator.helpers.DecimalFormatter
import com.tt.invoicecreator.ui.components.InputDigitsWithLabel
import com.tt.invoicecreator.ui.components.InputTextWithLabel
import com.tt.invoicecreator.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogItemCountDiscountAndCommentsV2(
    itemV2: ItemV2,
    viewModel: AppViewModel,
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

    val decimalFormatter = DecimalFormatter()

    BasicAlertDialog(
        onDismissRequest = {
            onDismissRequest()
        }
    ) {
        Column(
            modifier = Modifier
                .background(Color.LightGray),

            ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                text = "TITLE"
            )
            InputDigitsWithLabel(
                labelText = "Quantity",
                inputText = itemCount.value
            ) {
                itemCount.value = decimalFormatter.cleanup(it)
            }
            InputDigitsWithLabel(
                labelText = "DISCOUNT",
                inputText = itemDiscount.value
            ) {
                itemDiscount.value = decimalFormatter.cleanup(it)
            }
            InputTextWithLabel(
                labelText = "COMMENT",
                inputText = itemComment.value
            ) {
                itemComment.value = it
            }
            Button(
                enabled = itemCount.value.isNotEmpty() && itemDiscount.value.isNotEmpty(),
                onClick = {

                    viewModel.addItemToInvoice(
                        InvoiceItemV2(
                            itemV2 = itemV2,
                            itemCount = itemCount.value.toDouble(),
                            itemDiscount = itemDiscount.value.toDouble(),
                            comment = itemComment.value.trim()
                        )
                    )
                    navController.navigateUp()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                Text(
                    text = "SAVE"
                )

            }

        }

    }
}