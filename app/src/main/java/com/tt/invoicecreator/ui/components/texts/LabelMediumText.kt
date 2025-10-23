package com.tt.invoicecreator.ui.components.texts

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tt.invoicecreator.ui.theme.Typography
import com.tt.invoicecreator.ui.theme.myColors
import androidx.compose.material3.MaterialTheme

@Composable
fun LabelMediumText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
    text = text,
    modifier = modifier,
    style = Typography.bodyLarge,
    color = MaterialTheme.myColors.primaryDark
    )
}