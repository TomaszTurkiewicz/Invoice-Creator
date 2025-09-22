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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.roomV2.entities.InvoiceV2
import com.tt.invoicecreator.helpers.DateAndTime
import com.tt.invoicecreator.helpers.DecimalFormatter
import com.tt.invoicecreator.helpers.InvoiceDueDate
import com.tt.invoicecreator.helpers.InvoiceNumber
import com.tt.invoicecreator.ui.components.InputDigitsWithLabel
import com.tt.invoicecreator.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogInvoiceNumberV2(
    onDismissRequest: () -> Unit,
    viewModel: AppViewModel,
    modePro: Boolean,
    listOfThisMonthAndYearInvoices: List<InvoiceV2>?
) {


    val availableNumbers = remember {
        mutableListOf<Int>()
    }
    val decimalFormatter = DecimalFormatter()

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

    val numberExist = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(true) {
        var number = 1
        if(listOfThisMonthAndYearInvoices!=null){
            while (availableNumbers.size<5){

                var numberUsed = false
                for(j in listOfThisMonthAndYearInvoices){
                    if(j.invoiceNumber == number){
                        numberUsed = true
                        break
                    }
                }

                if(!numberUsed){
                    availableNumbers.add(number)
                    number++
                }else{
                    number++
                }
            }
        }
        else{
            availableNumbers.add(1)
            availableNumbers.add(2)
            availableNumbers.add(3)
            availableNumbers.add(4)
            availableNumbers.add(5)
        }
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

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ){
                InputDigitsWithLabel(
                    modifier = Modifier
                        .fillMaxWidth(0.7f),
                    labelText = "NEW INVOICE NUMBER",
                    inputText = newNumber.value,
                    isError = newNumber.value == 0.toString() || newNumber.value == "" || numberExist.value,
                    errorText = when (newNumber.value) {
                        0.toString() -> {
                            "number cannot be 0"
                        }
                        "" -> {
                            "not valid number"
                        }
                        else -> {
                            "number already exists"
                        }
                    }
                ) {
                    numberExist.value = false
                    newNumber.value = decimalFormatter.cleanup(it,false)
                    if(newNumber.value != "" && newNumber.value != "0"){
                        listOfThisMonthAndYearInvoices?.forEach{ invoice ->
                            if(invoice.invoiceNumber == newNumber.value.toInt()){
                                numberExist.value = true
                            }
                        }
                    }
                }

                Text(
                    text = InvoiceNumber.getStringMonthAndYear(viewModel.getInvoiceV2().time),
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                )
            }
            if(availableNumbers.size == 5){
                Text(
                    text = "available numbers:${availableNumbers[0]}, ${availableNumbers[1]}, ${availableNumbers[2]}, ${availableNumbers[3]}, ${availableNumbers[4]}"
                )
            }






            /** due date **/
            if(modePro){
                Column {
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
                            modifier = Modifier
                                .fillMaxWidth(),
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
                    viewModel.calculateDueDate = false
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
                enabled = !dueDateActive.value
                        || dueDateString.value != ""
                        && newNumber.value != 0.toString()
                        && newNumber.value != ""
                        && !numberExist.value
            ) {
                Text(
                    text = "SAVE"
                )

            }
        }

    }

}