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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.SharedPreferences
import com.tt.invoicecreator.ui.components.InputTextWithLabel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogPaymentMethod(
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    val paymentMethod = remember {
        mutableStateOf(SharedPreferences.readPaymentMethod(context))
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

            InputTextWithLabel(
                labelText = "NEW PAYMENT METHOD",
                inputText = paymentMethod.value
            ) {
                paymentMethod.value = it
            }
            Button(
                enabled = paymentMethod.value?.trim()!!.isNotEmpty(),
                onClick = {
                    SharedPreferences.savePaymentMethod(context,paymentMethod.value!!)
                    onDismissRequest()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ){
                Text(
                    text = "SAVE"
                )
            }
        }
    }
}