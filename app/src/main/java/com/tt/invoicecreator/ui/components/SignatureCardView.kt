package com.tt.invoicecreator.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.tt.invoicecreator.data.SignatureFile
import com.tt.invoicecreator.ui.components.texts.BodyLargeText
import com.tt.invoicecreator.ui.components.texts.TitleLargeText
import com.tt.invoicecreator.ui.theme.myColors
import java.io.File

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
    ){
        if(imageBitmap != null){
            Row(){
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

        }else{
            TitleLargeText(
                text = "NO SIGNATURE YET",
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

    }
}