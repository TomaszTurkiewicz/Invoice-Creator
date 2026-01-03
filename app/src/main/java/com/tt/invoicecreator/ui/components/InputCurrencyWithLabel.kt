package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.helpers.Currency
import com.tt.invoicecreator.ui.components.texts.LabelMediumText
import com.tt.invoicecreator.ui.theme.myColors

@Composable
fun InputCurrencyWithLabel(
    labelText: String,
    selectedCurrency: Currency,
    chooser:Boolean = true,
    alertDialog: MutableState<Boolean>,
    onCurrencyClicked: () -> Unit
)
{
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ){
        LabelMediumText(
            text = labelText
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = "${selectedCurrency.name} (${selectedCurrency.symbol})",
            onValueChange = {},
            readOnly = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.myColors.primaryDark,
                unfocusedTextColor = MaterialTheme.myColors.primaryDark,
                focusedContainerColor = MaterialTheme.myColors.material.primaryContainer,
                unfocusedContainerColor = MaterialTheme.myColors.material.primaryContainer
            ),
            trailingIcon = {
                if(chooser){
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Currency",
                        modifier = Modifier
                            .clickable{
                                onCurrencyClicked()
                            },
                        tint = MaterialTheme.myColors.primaryDark
                    )
                }
            },
            interactionSource = remember { MutableInteractionSource() }
                .also { interactionSource ->
                    LaunchedEffect(interactionSource){
                        interactionSource.interactions.collect{
                            if(it is PressInteraction.Release){
                                if(chooser){
                                    onCurrencyClicked()
                                }else{
                                    alertDialog.value = true
                                }
                            }
                            }
                    }
                }
        )
    }
}