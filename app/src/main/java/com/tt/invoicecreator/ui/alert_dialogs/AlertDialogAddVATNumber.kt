package com.tt.invoicecreator.ui.alert_dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.SharedPreferences
import com.tt.invoicecreator.ui.components.CustomButton
import com.tt.invoicecreator.ui.components.InputTextWithLabel
import com.tt.invoicecreator.ui.components.cards.CustomCardView
import com.tt.invoicecreator.ui.components.texts.TitleLargeText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogAddVATNumber(
    title:String,
    onDismissRequest: () -> Unit,
    saveVatNumber: (String) -> Unit,
    moveSwitch: () -> Unit
) {
    val vatNumber = remember {
        mutableStateOf("")
    }

    val context = LocalContext.current
    val user = remember {
        mutableStateOf(SharedPreferences.readUserDetails(context))
    }

    BasicAlertDialog(
        onDismissRequest = {
            // no dismiss
        }
    ) {
        CustomCardView {
            Column(
                modifier = Modifier
            ){
                TitleLargeText(
                    text = title,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .align(Alignment.CenterHorizontally),
                )
                InputTextWithLabel(
                    labelText = "VAT Number",
                    inputText = vatNumber.value
                ) {
                    vatNumber.value = it
                }

                CustomButton(
                    enabled = vatNumber.value.isNotEmpty(),
                    onClick = {
                        saveVatNumber(vatNumber.value)
                        onDismissRequest()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .align(Alignment.CenterHorizontally),
                    text = "SAVE"
                )

                CustomButton(
                    enabled = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .align(Alignment.CenterHorizontally),
                    text = "CANCEL",
                    makeItWarning = true,
                    onClick = {
                        moveSwitch()
                        onDismissRequest()
                    }
                )
            }
        }
    }
}
