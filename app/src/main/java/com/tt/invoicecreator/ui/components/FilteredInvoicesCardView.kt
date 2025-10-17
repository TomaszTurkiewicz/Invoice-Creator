package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

@Composable
fun FilteredInvoicesCardView(
    header:String,
    message: String,
    count:Int,
    onClick:() -> Unit
) {
    CustomCardView(
        modifier = Modifier
            .clickable{
                onClick()
            }
    ) {
        Column(

        ) {
            Text(
                text = header
            )
            Row(){
                Text(
                    text = message,
                    modifier = Modifier
                )
                Text(
                    text = count.toString(),
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            }
        }

    }
}