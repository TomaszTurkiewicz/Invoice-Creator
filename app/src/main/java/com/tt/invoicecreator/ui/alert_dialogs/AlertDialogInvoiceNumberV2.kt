package com.tt.invoicecreator.ui.alert_dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.helpers.DateAndTime
import com.tt.invoicecreator.helpers.DecimalFormatter
import com.tt.invoicecreator.helpers.InvoiceDueDate
import com.tt.invoicecreator.ui.components.InputDigitsWithLabel
import com.tt.invoicecreator.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogInvoiceNumberV2(
    onDismissRequest: () -> Unit,
    viewModel: AppViewModel,
    modePro: Boolean
) {
    val newNumber = remember {
        mutableStateOf(viewModel.getInvoiceV2().invoiceNumber.toString())
    }
    val dueDateActive = remember {
        mutableStateOf(true)
    }
    val dueDateString = remember {
        mutableStateOf(
            DateAndTime.getDifferenceInDays(
                viewModel.getInvoiceV2().time,
                viewModel.getInvoiceV2().dueDate ?: viewModel.getInvoiceV2().time
            ).toString()
        )
    }
    val dueDateLong = remember {
        mutableLongStateOf(
            InvoiceDueDate.getDueDate(viewModel.getInvoiceV2().time,DateAndTime.getDifferenceInDays(
                viewModel.getInvoiceV2().time,
                viewModel.getInvoiceV2().dueDate ?: viewModel.getInvoiceV2().time
            ))
        )
    }
    BasicAlertDialog(
        onDismissRequest = {
            onDismissRequest()
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
                labelText = "NEW INVOICE NUMBER",
                inputText = newNumber.value
            ) {
                newNumber.value = it
            }

            /** due date **/
            if(modePro){
                Column(){
                    Row{
                      Text(
                          text = "due date"
                      )
                        Switch(
                            checked = dueDateActive.value,
                            onCheckedChange = {
                                dueDateActive.value = it
                            }
                        )
                    }

                    if(dueDateActive.value){
                        InputDigitsWithLabel(
                            labelText = "PAYMENT IN...",
                            inputText = dueDateString.value,
                            isError = dueDateString.value.isEmpty()
                        ) {
                            dueDateString.value = DecimalFormatter().cleanup(it,false)
                            if(dueDateString.value != ""){
                                val days = dueDateString.value.toInt()
                                dueDateLong.longValue = InvoiceDueDate.getDueDate(viewModel.getInvoiceV2().time,days)
                            }
                            else{

                                dueDateLong.longValue = InvoiceDueDate.getDueDate(viewModel.getInvoiceV2().time,0)
                            }

                        }
                    }
                }
            }

            Button(
                onClick = {
                    viewModel.getInvoiceV2().invoiceNumber = newNumber.value.toInt()
                    viewModel.calculateNumber = false
                    if(dueDateActive.value){
                        viewModel.getInvoiceV2().dueDate = dueDateLong.longValue
                    }else{
                        viewModel.getInvoiceV2().dueDate = null
                    }
                    onDismissRequest()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                enabled = !dueDateActive.value || dueDateString.value.isNotEmpty()
            ) {
                Text(
                    text = "SAVE"
                )

            }
        }

    }

}

//todo check if new number not equal to "0"
// todo check if new number available (this month and year)
// todo show available numbers for this month and year