@file:Suppress("UNCHECKED_CAST")

package com.tt.invoicecreator.ui.screens

import android.app.Activity
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.createBitmap
import androidx.navigation.NavController
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.data.PathState
import com.tt.invoicecreator.data.SignatureFile
import com.tt.invoicecreator.ui.components.CustomButton
import com.tt.invoicecreator.ui.components.DrawingCanvas

@Composable
fun SignatureScreen(
    ignoredOnComposing: (AppBarState) -> Unit,
    navController: NavController
) {
    val context = LocalContext.current
    val activity = context as? Activity

    DisposableEffect(Unit) {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    val path = remember {
        mutableStateListOf<PathState>()
    }
    val drawBrush = remember {
        mutableStateOf(5f)
    }

    val drawColor = remember {
        mutableStateOf(Color.Black)
    }
    val usedColors = remember {
        mutableStateOf(mutableSetOf(Color.Black,Color.White, Color.Gray))
    }

    val capturingViewBounds = remember {
        mutableStateOf<Rect?>(null)
    }

    LaunchedEffect(key1 = true) {
        ignoredOnComposing(
            AppBarState(
                title = "SIGNATURE",
                action = null
            )
        )
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .onGloballyPositioned {
            capturingViewBounds.value = it.boundsInRoot()
        }
        .background(Color.White)){
        DrawingCanvas(
            drawColor = drawColor,
            drawBrush = drawBrush,
            usedColors = usedColors,
            paths = path
        )
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            CustomButton(
                modifier = Modifier.weight(1f),
                text = "CLEAR",
                onClick = {
                    path.clear()
                }
            )

            CustomButton(
                enabled = path.isNotEmpty(),
                modifier = Modifier.weight(1f),
                text = "SUBMIT",
                onClick = {

                    val bitmap = createBitmap(1200, 600, Bitmap.Config.ARGB_8888)

                    bitmap.applyCanvas {
                        drawColor(android.graphics.Color.WHITE)

                        val paint = android.graphics.Paint().apply {
                            color = android.graphics.Color.BLACK
                            style = android.graphics.Paint.Style.STROKE
                            strokeWidth = 8f // Bold enough for printing
                            strokeCap = android.graphics.Paint.Cap.ROUND
                            strokeJoin = android.graphics.Paint.Join.ROUND
                            isAntiAlias = true
                        }

                        path.forEach { pathState ->
                            paint.color = pathState.color.toArgb()
                            paint.strokeWidth = pathState.stroke
                            drawPath(pathState.path.asAndroidPath(), paint)
                        }
                    }

                    SignatureFile.saveSignature(context, bitmap)
                    navController.popBackStack()
                }
            )
        }
    }
}