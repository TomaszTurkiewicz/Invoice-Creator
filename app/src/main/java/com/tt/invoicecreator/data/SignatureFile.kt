package com.tt.invoicecreator.data

import android.content.Context
import com.tt.invoicecreator.R
import java.io.File

class SignatureFile {
    companion object{
        fun getFilePath(context: Context):String{
            return getOutputDirectory(context) + "/" + photoFileName()
        }

        private fun photoFileName(): String{
            return "signature.jpg"
        }

        private fun getOutputDirectory(context: Context):String{
            val mediaDir = context.getExternalFilesDir(null)?.let{
                File(it,context.resources.getString(R.string.app_name)).apply { mkdirs() }
            }
            return if(mediaDir !=null && mediaDir.exists()) mediaDir.absolutePath else context.filesDir.absolutePath
        }
    }
}