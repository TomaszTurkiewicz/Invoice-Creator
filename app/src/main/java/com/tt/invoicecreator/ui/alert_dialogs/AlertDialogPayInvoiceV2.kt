package com.tt.invoicecreator.ui.alert_dialogs

import android.annotation.SuppressLint
import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.AppUiState
import com.tt.invoicecreator.data.roomV2.entities.InvoiceItemV2
import com.tt.invoicecreator.data.roomV2.entities.PaidV2
import com.tt.invoicecreator.helpers.DateAndTime
import com.tt.invoicecreator.helpers.DecimalFormatter
import com.tt.invoicecreator.ui.components.InputDigitsWithLabel
import com.tt.invoicecreator.viewmodel.AppViewModel

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogPayInvoiceV2(
    viewModel: AppViewModel,
    invoiceId:Int,
    uiState: AppUiState,
    paidListV2: List<PaidV2>?,
    invoiceValue: Double,
    paid: Double,
    closeAlertDialog: () -> Unit
) {

    val decimalFormatter = DecimalFormatter()

    val amountPaid = remember {
        mutableStateOf("")
    }
    val comments = remember {
        mutableStateOf("")
    }
    val datePicker = remember {
        mutableStateOf(false)
    }

    val timeMilisec = remember {
        mutableLongStateOf(System.currentTimeMillis())
    }


    LaunchedEffect(key1 = true) {
        viewModel.updatePaymentDay(timeMilisec.longValue)
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
                modifier = Modifier
                    .fillMaxWidth(),
                labelText = "amount",
                inputText = amountPaid.value,
                isError = if(amountPaid.value == ""){
                    invoiceValue < paid + 0.0
                } else {
                    invoiceValue < paid + amountPaid.value.toDouble()
                }
            ) {
                amountPaid.value = decimalFormatter.cleanup(it)
            }

            Text(
                text = uiState.paymentDay,
                modifier = Modifier
                    .clickable{
                        datePicker.value = true
                    }
            )

            InputDigitsWithLabel(
                modifier = Modifier
                    .fillMaxWidth(),
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
                            time = timeMilisec.longValue,
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
                            time = timeMilisec.longValue,
                            notes = comments.value
                        )
                    )
                    viewModel.updatePaidListV2(newPaidList)
                    closeAlertDialog()
                },
                enabled = if(amountPaid.value == ""){
                    !(invoiceValue < paid + 0.0)
                } else {
                    !(invoiceValue < paid + amountPaid.value.toDouble())
                }
            ) {
                Text(
                    text = "SAVE"
                )
            }

        }
    }

    if(datePicker.value){
        val datePickerState = rememberDatePickerState()
        val confirmEnabled = derivedStateOf { datePickerState.selectedDateMillis != null }

        DatePickerDialog(
           onDismissRequest = {datePicker.value = false},
            confirmButton = {
                TextButton(
                    onClick = {
                        datePicker.value = false
                        datePickerState.selectedDateMillis?.let {
                            timeMilisec.longValue = it
                            viewModel.updatePaymentDay(it)
                        }
                    },
                    enabled = confirmEnabled.value
                ) {
                    Text( "OKAY")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }

    }
}