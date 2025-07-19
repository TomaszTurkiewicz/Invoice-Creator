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
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.ui.components.InputDigitsWithLabel
import com.tt.invoicecreator.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogInvoiceNumber(
    onDismissRequest: () -> Unit,
    viewModel: AppViewModel
) {
    val newNumber = remember {
        mutableStateOf(viewModel.getInvoice().invoiceNumber.toString())
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

            Button(
                onClick = {
                    viewModel.getInvoice().invoiceNumber = newNumber.value.toInt()
                    viewModel.calculateNumber = false
                    onDismissRequest()
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