package com.tt.invoicecreator.ui.components.texts


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.tt.invoicecreator.ui.theme.Typography
import com.tt.invoicecreator.ui.theme.myColors

@Composable
fun BodyLargeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.myColors.primaryDark,
    textAlign: TextAlign = TextAlign.Start,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Text(
        text = text,
        modifier = modifier,
        style = Typography.bodyLarge,
        color = color,
        textAlign = textAlign,
        fontWeight = fontWeight
    )
}