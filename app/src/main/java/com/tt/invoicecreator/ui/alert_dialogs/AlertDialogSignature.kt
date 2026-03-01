package com.tt.invoicecreator.ui.alert_dialogs

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.applyCanvas
import com.tt.invoicecreator.data.PathState
import com.tt.invoicecreator.data.SignatureFile
import com.tt.invoicecreator.ui.components.CustomButton
import com.tt.invoicecreator.ui.components.DrawingCanvas
import com.tt.invoicecreator.ui.components.cards.CustomCardView
import com.tt.invoicecreator.ui.components.texts.TitleLargeText
import java.io.File
import java.io.FileOutputStream
import kotlin.math.roundToInt

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogSignature(
    onDismissRequest: () -> Unit
) {
    val paths = remember {
        mutableStateOf(mutableListOf<PathState>())
    }
    val drawBrush = remember {
        mutableFloatStateOf(5f)
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

    paths.value.add(PathState(Path(),drawColor.value,drawBrush.floatValue))


    val content = LocalContext.current

    BasicAlertDialog(
        onDismissRequest = {
            onDismissRequest()
        }
    ) {
        CustomCardView {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
//                    .height(240.dp)
                    .padding(5.dp)
                    .clip(RoundedCornerShape(10.dp))
//                    .background(Color.White)
            )
            {

                TitleLargeText(
                    text = "NEW SIGNATURE",
                    modifier = Modifier
                        .padding(bottom = 10.dp, top = 5.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )


                val view = LocalView.current

                Column(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
//                        .weight(0.7f)
                        .height(180.dp)
                        .onGloballyPositioned {
                            capturingViewBounds.value = it.boundsInRoot()
                        }
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    DrawingCanvas(
                        drawColor = drawColor,
                        drawBrush = drawBrush,
                        usedColors = usedColors,
                        paths = paths.value
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 5.dp)

//                        .weight(0.3f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    )
                    {
                        CustomButton(
                            onClick = {
                                paths.value = mutableListOf()
                                paths.value.add(PathState(Path(),drawColor.value,drawBrush.floatValue))
                            },
                            modifier = Modifier
                                .weight(1f),
                            text = "CLEAR"
                        )

                        CustomButton(
                            onClick = {
                                val bounds = capturingViewBounds.value ?: return@CustomButton

                                val bitmap = Bitmap.createBitmap(
                                    bounds.width.roundToInt(),
                                    bounds.height.roundToInt(),
                                    Bitmap.Config.ARGB_8888
                                )

                                // 2. Manually draw the signature onto a WHITE background
                                bitmap.applyCanvas {
                                    // Fill the background with WHITE for the PDF
                                    drawColor(android.graphics.Color.WHITE)

                                    // Draw each path from your 'paths' state
                                    val paint = android.graphics.Paint().apply {
                                        isAntiAlias = true
                                        style = android.graphics.Paint.Style.STROKE
                                        strokeJoin = android.graphics.Paint.Join.ROUND
                                        strokeCap = android.graphics.Paint.Cap.ROUND
                                    }

                                    paths.value.forEach { pathState ->
                                        paint.color = pathState.color.toArgb()
                                        paint.strokeWidth = pathState.stroke
                                        drawPath(pathState.path.asAndroidPath(), paint)
                                    }
                                }
                                saveSignature(content, bitmap)
                                onDismissRequest()
                            },
                                    //                            onClick = {
//                                val bounds = capturingViewBounds.value ?: return@CustomButton
//                                image.value =
//                                    createBitmap(
//                                        bounds.width.roundToInt(),
//                                        bounds.height.roundToInt()
//                                    ).applyCanvas {
//                                        translate(-bounds.left, -bounds.top)
//                                        view.draw(this)
//                                    }
//                                saveSignature(content, image.value!!)
//                                onDismissRequest()
//                            },
                            modifier = Modifier
                                .weight(1f),
                            text = "SUBMIT"
                        )
                    }
                }
            }
        }
    }
    }



fun saveSignature(context: Context,bitmap:Bitmap):String{
    val photoFile =SignatureFile.getFilePath(context)
    val path = File(photoFile)

    try {
        val fileOutputStream = FileOutputStream(path)
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream)
        fileOutputStream.close()
    }catch (e:Exception){
        e.printStackTrace()
        return ""
    }
    return path.path
}