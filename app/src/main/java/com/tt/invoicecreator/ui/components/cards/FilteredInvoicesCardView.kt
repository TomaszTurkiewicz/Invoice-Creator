package com.tt.invoicecreator.ui.components.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.ui.components.texts.BodyLargeText
import com.tt.invoicecreator.ui.components.texts.TitleLargeText

@Composable
fun FilteredInvoicesCardView(
    header:String,
    message: String,
    count:Int,
    modifier: Modifier = Modifier,
    onClick:() -> Unit
) {
    CustomCardView(
        modifier = modifier
            .clickable {
                onClick()
            }
    ) {
        Column {
            TitleLargeText(
                text = header,
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .fillMaxWidth()
            )
            Row {
                BodyLargeText(
                    text = message
                )
                BodyLargeText(
                    text = count.toString(),
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            }
        }

    }
}