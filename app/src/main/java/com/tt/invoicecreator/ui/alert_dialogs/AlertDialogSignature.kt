package com.tt.invoicecreator.ui.alert_dialogs

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.applyCanvas
import com.tt.invoicecreator.data.PathState
import com.tt.invoicecreator.ui.components.DrawingCanvas
import kotlin.math.roundToInt
import androidx.core.graphics.createBitmap
import com.tt.invoicecreator.data.SignatureFile
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogSignature(
    onDismissRequest: () -> Unit
) {
    val paths = remember {
        mutableStateOf(mutableListOf<PathState>())
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
    val image = remember {
        mutableStateOf<Bitmap?>(null)
    }

    paths.value.add(PathState(Path(),drawColor.value,drawBrush.value))


    val content = LocalContext.current

    BasicAlertDialog(
        onDismissRequest = {
            onDismissRequest()
        }
    ) {

        Column (
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(240.dp)
                .padding(5.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
        ) {


            val view = LocalView.current

            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .weight(0.7f)
                    .onGloballyPositioned {
                        capturingViewBounds.value = it.boundsInRoot()
                    }
            ) {
                DrawingCanvas(
                    drawColor = drawColor,
                    drawBrush = drawBrush,
                    usedColors = usedColors,
                    paths = paths.value
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                )
                {
                    Button(
                        onClick = {
                            paths.value = mutableListOf()
                        },
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(text = "CLEAR")
                    }

                    Button(
                        onClick = {
                            val bounds = capturingViewBounds.value ?: return@Button
                            image.value =
                                createBitmap(
                                    bounds.width.roundToInt(),
                                    bounds.height.roundToInt()
                                ).applyCanvas {
                                    translate(-bounds.left, -bounds.top)
                                    view.draw(this)
                                }
                            saveSignature(content, image.value!!)
                            onDismissRequest()
                        }
                    ) {
                        Text(text = "SUBMIT")
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