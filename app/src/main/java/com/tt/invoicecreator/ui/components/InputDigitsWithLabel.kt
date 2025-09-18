package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun InputDigitsWithLabel(
    modifier: Modifier,
    labelText: String,
    inputText: String?,
    isError: Boolean = false,
    onValueChange: (String) -> Unit,

) {
    Column(
        modifier = modifier
            .padding(10.dp)
    ){
        Text(
            text = labelText
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = inputText ?: "",
            onValueChange = {
                onValueChange(it)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            isError = isError
        )
    }
}