package com.tt.invoicecreator.data

import android.content.Context
import android.graphics.Bitmap
import com.tt.invoicecreator.R
import java.io.File
import java.io.FileOutputStream

class SignatureFile {
    companion object{
        fun getFilePath(context: Context):String{
            return getOutputDirectory(context) + "/signature.jpg"
        }

        private fun getOutputDirectory(context: Context):String{
            val mediaDir = context.getExternalFilesDir(null)?.let{
                File(it,context.resources.getString(R.string.app_name)).apply { mkdirs() }
            }
            return if(mediaDir !=null && mediaDir.exists()) mediaDir.absolutePath else context.filesDir.absolutePath
        }

        fun saveSignature(context: Context,bitmap:Bitmap):String{
            val photoFile = getFilePath(context)
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
    }
}