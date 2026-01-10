package com.tt.invoicecreator.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun InvoiceAnimatedIcon(
    onTwoCyclesFinished: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "invoice")

    val currentOnFinished by rememberUpdatedState(newValue = onTwoCyclesFinished)

    // Handle the 2-cycle timing (3.5s * 2 = 7s)
    LaunchedEffect(Unit) {
        delay(7000L)
        currentOnFinished()
    }
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "masterProgress"
    )

    // Colors based on your description
    val lightBlue = Color(0xFFE3F2FD)
    val brandBlue = Color(0xFF2196F3)
    val checkmarkGreen = Color(0xFF4CAF50)

    Canvas(
        modifier = Modifier.size(200.dp)
    ) {
        val width = size.width
        val height = size.height
        val centerX = width / 2
        val centerY = height / 2

        drawCircle(
            color = lightBlue,
            radius = width / 2
        )

        // 2. Draw White Paper Shape
        val paperWidth = width * 0.5f
        val paperHeight = height * 0.65f
        val paperLeft = centerX - (paperWidth / 2)
        val paperTop = centerY - (paperHeight / 2)

        drawRoundRect(
            color = Color.White,
            topLeft = Offset(paperLeft, paperTop),
            size = Size(paperWidth, paperHeight),
            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
        )

        val lineXStart = paperLeft + (paperWidth * 0.2f)
        val lineXEnd = paperLeft + (paperWidth * 0.8f)
        val fullLineWidth = lineXEnd - lineXStart

        // --- Static First Line (Always On) ---
        drawLine(
            color = brandBlue,
            start = Offset(lineXStart, paperTop + (paperHeight * 0.25f)),
            end = Offset(lineXEnd, paperTop + (paperHeight * 0.25f)),
            strokeWidth = 5.dp.toPx(),
            cap = StrokeCap.Round,
            alpha = if (progress > 0.8f) 0f else 1f // Hide during reset pause
        )

        // --- Animated Second Line (0.0 to 0.2) ---
        val line2Progress = ((progress - 0.0f) / 0.2f).coerceIn(0f, 1f)
        if (line2Progress > 0f && progress <= 0.8f) {
            drawLine(
                color = Color.LightGray,
                start = Offset(lineXStart, paperTop + (paperHeight * 0.45f)),
                end = Offset(lineXStart + (fullLineWidth * line2Progress), paperTop + (paperHeight * 0.45f)),
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        }

        // --- Animated Third Line (0.2 to 0.4) ---
        val line3Progress = ((progress - 0.2f) / 0.2f).coerceIn(0f, 1f)
        if (line3Progress > 0f && progress <= 0.8f) {
            drawLine(
                color = Color.LightGray,
                start = Offset(lineXStart, paperTop + (paperHeight * 0.65f)),
                end = Offset(lineXStart + (fullLineWidth * line3Progress), paperTop + (paperHeight * 0.65f)),
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        }

        // 4. Green Circle + Checkmark Stamp (0.4 to 0.7)
        val checkStart = 0.4f
        val checkEnd = 0.7f
        val checkProgress = ((progress - checkStart) / (checkEnd - checkStart)).coerceIn(0f, 1f)


        if (checkProgress > 0f && progress <= 0.8f) {
            val stampCenterX = paperLeft + paperWidth * 0.75f
            val stampCenterY = paperTop + paperHeight * 0.85f
            val stampRadius = 12.dp.toPx()

            // Draw Green Circle "Stamp"
            drawCircle(
                color = checkmarkGreen,
                radius = stampRadius * checkProgress,
                center = Offset(stampCenterX, stampCenterY)
            )

            // Draw Light Blue Checkmark inside the green circle
            if (checkProgress > 0.5f) {
                val checkInternalProg = ((checkProgress - 0.5f) / 0.5f)
                val path = Path().apply {
                    moveTo(stampCenterX - 5.dp.toPx(), stampCenterY)
                    lineTo(stampCenterX - 1.dp.toPx(), stampCenterY + 4.dp.toPx())
                    lineTo(stampCenterX + 6.dp.toPx(), stampCenterY - 4.dp.toPx())
                }
                drawPath(
                    path = path,
                    color = lightBlue,
                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round),
                    alpha = checkInternalProg
                )
            }
        }

    }

}