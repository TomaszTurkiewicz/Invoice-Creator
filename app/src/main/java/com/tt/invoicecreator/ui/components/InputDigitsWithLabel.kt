package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.ui.components.texts.BodyLargeText
import com.tt.invoicecreator.ui.components.texts.LabelMediumText
import com.tt.invoicecreator.ui.theme.myColors

@Composable
fun InputDigitsWithLabel(
    modifier: Modifier,
    labelText: String,
    inputText: String?,
    isError: Boolean = false,
    errorText: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit


) {
    Column(
        modifier = modifier
            .padding(10.dp)
    ){
        LabelMediumText(
            text = labelText
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = inputText ?: "",
            onValueChange = {
                onValueChange(it)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.myColors.primaryDark,
                unfocusedTextColor = MaterialTheme.myColors.primaryDark,
                focusedContainerColor = MaterialTheme.myColors.material.primaryContainer,
                unfocusedContainerColor = MaterialTheme.myColors.material.primaryContainer
            ),
            isError = isError,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon
        )
        if(isError){
            BodyLargeText(
                text = errorText,
                modifier = Modifier
                    .padding(start = 10.dp),
                color = MaterialTheme.colorScheme.error
            )
        }
        else{
            BodyLargeText(
                text = ""
            )
        }
    }
}