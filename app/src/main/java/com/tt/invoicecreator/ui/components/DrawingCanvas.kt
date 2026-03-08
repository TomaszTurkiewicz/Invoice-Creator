package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import com.tt.invoicecreator.data.PathState

@Composable
fun DrawingCanvas(
    drawColor: MutableState<Color>,
    drawBrush: MutableState<Float>,
    usedColors: MutableState<MutableSet<Color>>,
    paths: MutableList<PathState>
) {

    val currentPath = remember {
        mutableStateOf<Path?>(null)
    }

    var motionEventTick by remember { mutableLongStateOf(0L) }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        val newPath = Path().apply {
                            moveTo(offset.x, offset.y)
                        }
                        currentPath.value = newPath
                        usedColors.value.add(drawColor.value)
                    },
                    onDrag = { change, _ ->
                        currentPath.value?.lineTo(change.position.x, change.position.y)
                        motionEventTick++
                    },
                    onDragEnd = {
                        currentPath.value?.let { path ->
                            paths.add(PathState(path, drawColor.value, drawBrush.value))
                        }
                        currentPath.value = null
                    }
                )
            }){

        val _tick = motionEventTick

        paths.forEach{
            drawPath(
                path = it.path,
                color = it.color,
                style = Stroke(
                    width = it.stroke,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round)
            )
        }

        currentPath.value?.let {
            drawPath(
                path = it,
                color = drawColor.value,
                style = Stroke(
                    width = drawBrush.value,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
    }
}