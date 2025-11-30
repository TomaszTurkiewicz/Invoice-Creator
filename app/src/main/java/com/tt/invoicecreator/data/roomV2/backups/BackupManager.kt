package com.tt.invoicecreator.data.roomV2.backups

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import com.google.gson.Gson
import com.tt.invoicecreator.data.SharedPreferences
import com.tt.invoicecreator.data.SignatureFile
import com.tt.invoicecreator.viewmodel.AppViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.OutputStream

object BackupManager {
    fun exportDatabaseToJson(
        context: Context,
        viewModel: AppViewModel,
        onProgress: (String?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 1. Fetch all data from Room (Using ViewModel or DAO directly)
                // Note: You might need to add 'suspend' functions to your DAO or use .first() on Flows
                // This assumes your ViewModel exposes lists or you can access DAOs.
                // For this example, let's assume we access lists via ViewModel helpers or DAOs.
                onProgress("Exporting Invoices...")
                val allInvoices = viewModel.getAllInvoicesDirectly()

                onProgress("Exporting Invoice Items...")
                val allInvoiceItems = viewModel.getAllInvoiceItemsDirectly()

                onProgress("Exporting Items...")
                val allItems = viewModel.getAllItemsDirectly()

                onProgress("Exporting Clients...")
                val allClients = viewModel.getAllClientsDirectly()

                onProgress("Exporting Paid Status...")
                val allPaid = viewModel.getAllPaidDirectly()

                onProgress("Exporting User Details...")
                val user = SharedPreferences.readUserDetails(context)
                val paymentMethod = SharedPreferences.readPaymentMethod(context)

                onProgress("Exporting Signature...")
                val signaturePAth = SignatureFile.getFilePath(context)
                val signatureBase64 = convertFileToBase64(signaturePAth)

                // 2. Create the Backup Object
                onProgress("Creating Backup Object...")
                val backupData = BackupData(
                    invoices = allInvoices,
                    invoiceItems = allInvoiceItems,
                    items = allItems,
                    clients = allClients,
                    paid = allPaid,
                    user = user,
                    paymentMethod = paymentMethod,
                    signatureBase64 = signatureBase64
                )

                // 3. Convert to JSON
                onProgress("Saving File...")
                val gson = Gson()
                val jsonString = gson.toJson(backupData)

                // 4. Save File
                val fileName = "InvoiceCreator_Backup_${System.currentTimeMillis()}.json"
                saveFileToDownloads(context, fileName, jsonString)

                onProgress(null)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Backup saved to Downloads!", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Export failed: ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun saveFileToDownloads(
        context: Context,
        fileName: String,
        content: String
    ) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/json")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI,contentValues)

            uri?.let {
                val outputStream : OutputStream? = resolver.openOutputStream(it)
                outputStream?.use { stream ->
                    stream.write(content.toByteArray())
                }
            }

        }
        else{
            val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if(!downloadDir.exists()) downloadDir.mkdirs()
            val file = File(downloadDir, fileName)
            FileOutputStream(file).use { stream ->
                stream.write(content.toByteArray())
            }
        }
    }

    fun importDatabaseFromJson(
        context: Context,
        uri: Uri,
        viewModel: AppViewModel,
        onProgress: (String?) -> Unit
    ){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                onProgress("Reading File...")
                val content = StringBuilder()
                context.contentResolver.openInputStream(uri)?.use {inputStream ->
                    BufferedReader(InputStreamReader(inputStream)).use { reader ->
                        var line:String? = reader.readLine()
                        while( line != null){
                            content.append(line)
                            line = reader.readLine()
                        }

                        }
                }
                val jsonString = content.toString()
                val gson = Gson()
                val backupData = gson.fromJson(jsonString, BackupData::class.java)

                viewModel.clearAllTables()
                // Restore Invoices
                onProgress("Restoring Invoices...")
                backupData.invoices.forEach { viewModel.insertWithIdInvoiceV2(it) }

                // Restore Items
                onProgress("Restoring Items...")
                backupData.items.forEach { viewModel.insertWithIdItemV2(it) } // Assuming saveItemV2 handles insert/update

                // Restore Invoice Items (Links)
                onProgress("Restoring Invoice Items...")
                backupData.invoiceItems.forEach { viewModel.insertWithIdInvoiceItemV2(it) }

                // Restore Clients
                onProgress("Restoring Clients...")
                backupData.clients.forEach { viewModel.insertWithIdClientV2(it) }

                // Restore Paid status
                onProgress("Restoring Paid Status...")
                backupData.paid.forEach { viewModel.insertWithIdPaidV2(it) }

                // Restore User Details
                onProgress("Restoring User Details...")
                SharedPreferences.saveUserDetails(context,backupData.user)

                // Restore Payment Method
                onProgress("Restoring Payment Method...")
                if(backupData.paymentMethod != null) {
                    SharedPreferences.savePaymentMethod(context, backupData.paymentMethod)
                }

                // Restore Signature
                onProgress("Restoring Signature...")
                if(backupData.signatureBase64 != null){
                    val signaturePath = SignatureFile.getFilePath(context)
                    convertBase64ToFile(backupData.signatureBase64,signaturePath)
                }

                onProgress(null)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Backup Restored Successfully!", Toast.LENGTH_LONG).show()
                }
            }catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Import Failed: ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun convertBase64ToFile(base64String: String, filePath: String) {
        try {
            val bytes = Base64.decode(base64String, Base64.DEFAULT)
            val file = File(filePath)

            file.parentFile?.mkdirs()
            FileOutputStream(file).use { stream ->
                stream.write(bytes)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            }
        }

    private fun convertFileToBase64(filePath: String): String? {
        val file = File(filePath)
        if(!file.exists()) return null

        return try {
            val bytes = file.readBytes()
            Base64.encodeToString(bytes, Base64.DEFAULT)
            } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    }
