package com.tt.invoicecreator.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp

@Composable
fun SignatureCardView(
    onClick: () -> Unit,
    imageBitmap: Bitmap?
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                onClick()
            },
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ){
        if(imageBitmap != null){
            Image(
                modifier = Modifier
                    .fillMaxWidth(),
                bitmap = imageBitmap.asImageBitmap(),
                contentDescription = ""
            )
        }else{
            Text(
                text = "NO SIGNATURE YET",
                modifier = Modifier
                    .padding(20.dp)
            )
        }

    }
}