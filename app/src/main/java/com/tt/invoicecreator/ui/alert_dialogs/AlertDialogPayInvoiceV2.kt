package com.tt.invoicecreator.ui.alert_dialogs

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.AppUiState
import com.tt.invoicecreator.data.roomV2.entities.PaidV2
import com.tt.invoicecreator.helpers.Currency
import com.tt.invoicecreator.helpers.DecimalFormatter
import com.tt.invoicecreator.helpers.InvoiceValueCalculator
import com.tt.invoicecreator.ui.components.CustomButton
import com.tt.invoicecreator.ui.components.InputDigitsWithLabel
import com.tt.invoicecreator.ui.components.cards.CustomCardView
import com.tt.invoicecreator.ui.components.texts.BodyLargeText
import com.tt.invoicecreator.ui.components.texts.TitleLargeText
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
    paidInvoicesCollection: List<PaidV2>?,
    currency: Currency,
    closeAlertDialog: () -> Unit
) {

    val decimalFormatter = DecimalFormatter()

    val amountPaid = remember {
        mutableStateOf("0")
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

    val newPaidList = paidInvoicesCollection?.filter {
        it.invoiceId == invoiceId
    }

    val paidAmountAlready = InvoiceValueCalculator.calculatePaid(newPaidList)


    LaunchedEffect(key1 = true) {
        viewModel.updatePaymentDay(timeMilisec.longValue)
    }


    BasicAlertDialog(
        onDismissRequest = {
            closeAlertDialog()
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
                text = "ADD PAYMENT"
            )
            InputDigitsWithLabel(
                modifier = Modifier
                    .fillMaxWidth(),
                labelText = "AMOUNT",
                inputText = amountPaid.value,
                leadingIcon = if (currency.prefix) {
                    { BodyLargeText(currency.symbol) }
                } else null,
                trailingIcon = if (!currency.prefix) {
                    { BodyLargeText(currency.symbol) }
                } else null,
                isError = if(amountPaid.value == ""){
                    true
                } else {
                    invoiceValue < paidAmountAlready + amountPaid.value.toDouble()
//                    invoiceValue < paid + amountPaid.value.toDouble()
                },
                errorText = if(amountPaid.value == ""){
                    "not valid amount"
                } else {
                    "paid too much"
                }
            ) {
                amountPaid.value = decimalFormatter.cleanup(it)
            }

            BodyLargeText(
                text = "PAID ON ${uiState.paymentDay}",
                modifier = Modifier
                    .padding(start = 10.dp)
                    .clickable{
                        datePicker.value = true
                    }
            )

            InputDigitsWithLabel(
                modifier = Modifier
                    .fillMaxWidth(),
                labelText = "COMMENTS",
                inputText = comments.value
            ) {
                comments.value = it
            }

            CustomButton(
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

                    closeAlertDialog()
                },
                enabled = if(amountPaid.value == ""){
                    false
                   // !(invoiceValue < paid + 0.0)
                } else {
                    !(invoiceValue < paidAmountAlready + amountPaid.value.toDouble())
       //             !(invoiceValue < paid + amountPaid.value.toDouble())
                },
                modifier = Modifier
                    .fillMaxWidth(),
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