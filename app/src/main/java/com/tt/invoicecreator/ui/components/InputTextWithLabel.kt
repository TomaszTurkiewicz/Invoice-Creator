package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.ui.components.texts.LabelMediumText

@Composable
fun InputTextWithLabel(
    labelText: String,
    inputText: String?,
    onValueChange: (String) -> Unit
) {
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
            value = inputText ?: "",
            onValueChange = {
                onValueChange(it)
            }
        )
    }
}