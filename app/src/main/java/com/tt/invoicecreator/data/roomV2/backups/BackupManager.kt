package com.tt.invoicecreator.data.roomV2.backups

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.google.gson.Gson
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
        viewModel: AppViewModel
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 1. Fetch all data from Room (Using ViewModel or DAO directly)
                // Note: You might need to add 'suspend' functions to your DAO or use .first() on Flows
                // This assumes your ViewModel exposes lists or you can access DAOs.
                // For this example, let's assume we access lists via ViewModel helpers or DAOs.
                val allInvoices = viewModel.getAllInvoicesDirectly()
                val allInvoiceItems = viewModel.getAllInvoiceItemsDirectly()
                val allItems = viewModel.getAllItemsDirectly()
                val allClients = viewModel.getAllClientsDirectly()
                val allPaid = viewModel.getAllPaidDirectly()


                // 2. Create the Backup Object
                val backupData = BackupData(
                    invoices = allInvoices,
                    invoiceItems = allInvoiceItems,
                    items = allItems,
                    clients = allClients,
                    paid = allPaid
                )

                // 3. Convert to JSON
                val gson = Gson()
                val jsonString = gson.toJson(backupData)

                // 4. Save File
                val fileName = "InvoiceCreator_Backup_${System.currentTimeMillis()}.json"
                saveFileToDownloads(context, fileName, jsonString)

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
        viewModel: AppViewModel
    ){
        CoroutineScope(Dispatchers.IO).launch {
            try {
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
                backupData.invoices.forEach { viewModel.insertWithIdInvoiceV2(it) }

                // Restore Items
                backupData.items.forEach { viewModel.insertWithIdItemV2(it) } // Assuming saveItemV2 handles insert/update

                // Restore Invoice Items (Links)
                backupData.invoiceItems.forEach { viewModel.insertWithIdInvoiceItemV2(it) }

                // Restore Clients
                backupData.clients.forEach { viewModel.insertWithIdClientV2(it) }

                // Restore Paid status
                backupData.paid.forEach { viewModel.insertWithIdPaidV2(it) }

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
}