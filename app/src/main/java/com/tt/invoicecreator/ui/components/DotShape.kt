package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun DotShape(
    enabled: Boolean
) {
    Canvas(
        modifier = Modifier.size(20.dp)
    ){
        drawCircle(
            color = Color.Black,
            style = Stroke(width = size.width*0.1f),
            radius = size.width*0.4f
        )
        if(enabled){
            drawCircle(
                color = Color.Black,
                style = Fill,
                radius = size.width*0.3f
            )

        }

    }
}