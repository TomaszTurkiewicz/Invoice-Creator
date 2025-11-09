package com.tt.invoicecreator.ui.components.cards

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import com.tt.invoicecreator.ui.components.texts.BodyLargeText
import com.tt.invoicecreator.ui.components.texts.TitleLargeText

@Composable
fun SignatureCardView(
    onClick: () -> Unit,
    imageBitmap: Bitmap?
) {
    CustomCardView(
        modifier = Modifier
            .clickable {
                onClick()
            }
            .aspectRatio(4f)
    ) {
        if (imageBitmap != null) {
            Row {
                BodyLargeText(
                    text = "SIGNATURE",
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                )
                Image(
                    modifier = Modifier
                        .weight(1f),
                    bitmap = imageBitmap.asImageBitmap(),
                    contentDescription = ""
                )
            }

        } else {
            TitleLargeText(
                text = "NO SIGNATURE YET",
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

    }
}