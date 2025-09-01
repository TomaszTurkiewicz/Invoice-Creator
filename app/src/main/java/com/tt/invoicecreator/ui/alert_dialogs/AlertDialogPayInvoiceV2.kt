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
import com.tt.invoicecreator.data.roomV2.entities.PaidV2
import com.tt.invoicecreator.helpers.DecimalFormatter
import com.tt.invoicecreator.ui.components.InputDigitsWithLabel
import com.tt.invoicecreator.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogPayInvoiceV2(
    viewModel: AppViewModel,
    invoiceId:Int,
    paidListV2: List<PaidV2>?,
    closeAlertDialog: () -> Unit
) {

    val decimalFormatter = DecimalFormatter()

    val amountPaid = remember {
        mutableStateOf("")
    }
    val yearPaid = remember {
        mutableStateOf("")
    }
    val monthPaid = remember {
        mutableStateOf("")
    }
    val dayPaid = remember {
        mutableStateOf("")
    }
    val comments = remember {
        mutableStateOf("")
    }
    BasicAlertDialog(
        onDismissRequest = {
            closeAlertDialog()
        }
    ) {
        Column(
            modifier = Modifier
                .background(Color.LightGray)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                text = "TITLE"
            )
            InputDigitsWithLabel(
                labelText = "amount",
                inputText = amountPaid.value
            ) {
                amountPaid.value = decimalFormatter.cleanup(it)
            }
            InputDigitsWithLabel(
                labelText = "year",
                inputText = yearPaid.value
            ) {
                yearPaid.value = decimalFormatter.cleanup(it)
            }
            InputDigitsWithLabel(
                labelText = "month",
                inputText = monthPaid.value
            ) {
                monthPaid.value = decimalFormatter.cleanup(it)
            }
            InputDigitsWithLabel(
                labelText = "day",
                inputText = dayPaid.value
            ) {
                dayPaid.value = decimalFormatter.cleanup(it)
            }
            InputDigitsWithLabel(
                labelText = "comments",
                inputText = comments.value
            ) {
                comments.value = it
            }

            Button(
                onClick = {
                    viewModel.savePayment(
                        PaidV2(
                            invoiceId = invoiceId,
                            amountPaid = amountPaid.value.toDouble(),
                            year = yearPaid.value.toInt(),
                            month = monthPaid.value.toInt(),
                            day = dayPaid.value.toInt(),
                            notes = comments.value
                        )
                    )
                    val newPaidList = mutableListOf<PaidV2>()
                    paidListV2?.forEach {
                        newPaidList.add(it)
                    }
                    newPaidList.add(
                        PaidV2(
                            invoiceId = invoiceId,
                            amountPaid = amountPaid.value.toDouble(),
                            year = yearPaid.value.toInt(),
                            month = monthPaid.value.toInt(),
                            day = dayPaid.value.toInt(),
                            notes = comments.value
                        )
                    )
                    viewModel.updatePaidListV2(newPaidList)
                    closeAlertDialog()
                }
            ) {
                Text(
                    text = "SAVE"
                )
            }

        }
    }
}