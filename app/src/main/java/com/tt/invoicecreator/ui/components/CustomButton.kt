package com.tt.invoicecreator.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tt.invoicecreator.ui.components.texts.BodyLargeText
import com.tt.invoicecreator.ui.theme.myColors

@Composable
fun CustomButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String
) {
    Button(
        onClick = {
            onClick()
        },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.myColors.primaryDark,
            contentColor = MaterialTheme.myColors.primaryLight,
        ),
        enabled = enabled
    ){
        BodyLargeText(
            text = text,
            color =MaterialTheme.myColors.material.background
        )
    }
}