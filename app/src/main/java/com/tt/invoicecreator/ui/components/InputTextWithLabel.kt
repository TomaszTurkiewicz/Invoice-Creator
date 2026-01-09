package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.ui.components.texts.LabelMediumText
import com.tt.invoicecreator.ui.theme.myColors

@Composable
fun InputTextWithLabel(
    labelText: String,
    inputText: String?,
    focusedContainerColor: Color = MaterialTheme.myColors.material.primaryContainer,
    unfocusedContainerColor: Color = MaterialTheme.myColors.material.primaryContainer,
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
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.myColors.primaryDark,
                unfocusedTextColor = MaterialTheme.myColors.primaryDark,
                focusedContainerColor = focusedContainerColor,
                unfocusedContainerColor = unfocusedContainerColor
            ),
            onValueChange = {
                onValueChange(it)
            }
        )
    }
}