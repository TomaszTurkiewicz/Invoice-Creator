package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.ui.theme.myColors

@Composable
fun DotShape(
    enabled: Boolean,
    color: Color
) {
    Canvas(
        modifier = Modifier
            .size(35.dp)
            .padding(5.dp)
    ){
        drawCircle(
            color = color,
            style = Stroke(width = size.width*0.1f),
            radius = size.width*0.4f
        )
        if(enabled){
            drawCircle(
                color = color,
                style = Fill,
                radius = size.width*0.3f
            )

        }

    }
}