package com.tt.invoicecreator.data.roomV2.backups

import android.content.Context
import android.net.Uri
import android.util.Base64
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.storage.storage
import com.google.gson.Gson
import com.tt.invoicecreator.data.SharedPreferences
import com.tt.invoicecreator.data.SignatureFile
import com.tt.invoicecreator.viewmodel.AppViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

object BackupManager {

    private val storage = Firebase.storage.reference
    private val auth = Firebase.auth

    fun getUserIdentifier(): String? {
        return auth.currentUser?.uid
    }

    suspend fun uploadToCloud(
        viewModel: AppViewModel,
        context: Context,
        onStatus: (String) -> Unit
    ) = withContext(Dispatchers.IO) {
        val userId = Firebase.auth.currentUser?.uid ?: return@withContext onStatus("not logged in")
        try {
            val json = generateJsonFromDatabase(viewModel, context, onStatus)
            val backupRef = storage.child("backups/$userId/database.json")

            onStatus("Uploading to cloud...")
            backupRef.putBytes(json.toByteArray()).await()

            onStatus("Cloud Export Successful")
        }
        catch (e: Exception) {
            onStatus("Cloud Export Failed: ${e.localizedMessage}")
        }
    }

    private suspend fun generateJsonFromDatabase(
        viewModel: AppViewModel,
        context: Context,
        onProgress: (String) -> Unit
    ): String = withContext(Dispatchers.IO){
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

        return@withContext Gson().toJson(backupData)
    }

    suspend fun downloadAndClearCloud(
        viewModel: AppViewModel,
        context: Context,
        onStatus: (String) -> Unit
    ) = withContext(Dispatchers.IO) {
        val userId = getUserIdentifier() ?: return@withContext onStatus("not logged in")
        val backupRef = storage.child("backups/$userId/database.json")
        try {
            onStatus("Downloading from cloud...")
            val bytes = backupRef.getBytes(1024 * 1024 * 20).await()
            val jsonString = String(bytes)

            importDatabaseFromJson(context, jsonString, viewModel, onStatus)
            backupRef.delete().await()
            onStatus("Cloud Import Successful")
        } catch (e: Exception) {
            onStatus("Cloud Import Failed: ${e.localizedMessage}")
        }
    }

    suspend fun importDatabaseFromJson(
        context: Context,
        jsonString: String,
        viewModel: AppViewModel,
        onProgress: (String) -> Unit
    ) = withContext(Dispatchers.IO) {
        try{
            val backupData = Gson().fromJson(jsonString, BackupData::class.java)
            performRestore(context, backupData, viewModel, onProgress)
        }
        catch (e: Exception) {
            onProgress("Error: ${e.localizedMessage}")
        }
    }

    private suspend fun performRestore(
        context: Context,
        backupData: BackupData,
        viewModel: AppViewModel,
        onProgress: (String) -> Unit
    ) = withContext(Dispatchers.IO) {
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

        onProgress("")
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "Backup Restored Successfully!", Toast.LENGTH_LONG).show()
        }
    }

    fun importDatabaseFromUri(
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

                val backupData = Gson().fromJson(content.toString(), BackupData::class.java)
                performRestore(context, backupData, viewModel, onProgress)

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

    private fun convertFileToBase64(filePath: String): String? {
        val file = File(filePath)
        if (!file.exists()) return null
        return try {
            Base64.encodeToString(file.readBytes(), Base64.DEFAULT)
        } catch (e: Exception) {
            null
        }
    }
    private fun convertBase64ToFile(base64: String, filePath: String) {
        try {
            val bytes = Base64.decode(base64, Base64.DEFAULT)
            val file = File(filePath)
            // Ensure parent directory exists
            file.parentFile?.mkdirs()
            file.writeBytes(bytes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    }
