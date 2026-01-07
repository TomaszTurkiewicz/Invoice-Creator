package com.tt.invoicecreator.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.filled.Unsubscribe
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.qonversion.android.sdk.Qonversion
import com.qonversion.android.sdk.dto.QonversionError
import com.qonversion.android.sdk.dto.entitlements.QEntitlement
import com.qonversion.android.sdk.listeners.QonversionEntitlementsCallback
import com.tt.invoicecreator.InvoiceCreatorScreen
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.data.SharedPreferences
import com.tt.invoicecreator.data.roomV2.backups.BackupManager
import com.tt.invoicecreator.data.roomV2.entities.ClientV2
import com.tt.invoicecreator.data.roomV2.entities.ItemV2
import com.tt.invoicecreator.helpers.FilterClients
import com.tt.invoicecreator.helpers.SettingsSection
import com.tt.invoicecreator.ui.alert_dialogs.AlertDialogAddMainUser
import com.tt.invoicecreator.ui.components.CustomButton
import com.tt.invoicecreator.ui.components.ExpandableCard
import com.tt.invoicecreator.ui.components.cards.ExportImportDataCardView
import com.tt.invoicecreator.ui.components.texts.BodyLargeText
import com.tt.invoicecreator.ui.theme.myColors
import com.tt.invoicecreator.viewmodel.AppViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun Settings(
    ignoredOnComposing: (AppBarState) -> Unit,
    modePro: Boolean,
    viewModel: AppViewModel,
    listOfClients: List<ClientV2>?,
    listOfItems: List<ItemV2>?,
    navController: NavController
) {
    val context = LocalContext.current
    val user = SharedPreferences.readUserDetails(context)
    val scope = rememberCoroutineScope()

    val loadingStatus = remember {
        mutableStateOf<String?>(null)
    }

    val itemListInUse = listOfItems?.filter {
        it.inUse
    }

    viewModel.navigateFromSettings()

    var expandedSection by remember { mutableStateOf(SettingsSection.NONE) }

    val clientsInUse = FilterClients.getInUse(listOfClients)

    LaunchedEffect(key1 = true) {
        ignoredOnComposing(
            AppBarState(
                title = "SETTINGS",
                action = null
            )
        )
    }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            scope.launch(Dispatchers.IO) {
                try {
                    loadingStatus.value = "Preparing import..."
                    BackupManager.importDatabaseFromJson(
                        context,
                        selectedUri,
                        viewModel
                    ) { statusText ->
                        scope.launch(Dispatchers.Main) {
                            loadingStatus.value = statusText
                        }

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Import failed", Toast.LENGTH_SHORT).show()
                    }
                } finally {
                    loadingStatus.value = null
                }
            }
        }
    }

    val alertDialogUpdateUser = remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        )
        {
            ExpandableCard(
                title = if(user.userName.isNullOrEmpty()) "Set User Profile" else user.userName,
                icon = Icons.Default.Person,
                isExpanded = expandedSection == SettingsSection.USER,
                onExpandClick = {
                    expandedSection = if(expandedSection == SettingsSection.USER) {
                        SettingsSection.NONE
                    } else {
                        SettingsSection.USER
                    }
                }
            )
            {

                Column(
                    modifier = Modifier
                        .clickable(
                            onClick = {
                                alertDialogUpdateUser.value = true
                            }
                        )
                ) {
                    BodyLargeText(
                        text = user.userAddressLine1 ?: "",
                        modifier = Modifier
                            .padding(start = 10.dp, top = 5.dp, bottom = 5.dp)
                    )
                    BodyLargeText(
                        text = user.userAddressLine2 ?: "",
                        modifier = Modifier
                            .padding(start = 10.dp, top = 5.dp, bottom = 5.dp)
                    )
                    BodyLargeText(
                        text = user.userCity ?: "",
                        modifier = Modifier
                            .padding(start = 10.dp, top = 5.dp, bottom = 10.dp)
                    )
                }
            }


            ExpandableCard(
                title = "CHANGING DEVICE",
                icon = Icons.Default.CloudUpload,
                isExpanded = expandedSection == SettingsSection.BACKUP,
                onExpandClick = {
                    expandedSection = if(expandedSection == SettingsSection.BACKUP) {
                        SettingsSection.NONE
                    } else {
                        SettingsSection.BACKUP
                    }
                }
            )
            {
                ExportImportDataCardView(
                    modePro = modePro,
                    context = context,
                    viewModel = viewModel,
                    importLauncher = importLauncher,
                    onExportClick =
                        {
                            scope.launch(Dispatchers.IO)
                            {
                                try {
                                    loadingStatus.value = "Starting export..."
                                    BackupManager.exportDatabaseToJson(
                                        context,
                                        viewModel
                                    ) { statusText ->
                                        scope.launch(Dispatchers.Main) {
                                            loadingStatus.value = statusText
                                        }
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(context, "Export failed", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                } finally {
                                    loadingStatus.value = null
                                }
                            }
                        }
                )
            }

            ExpandableCard(
                title = if(modePro) "UNSUBSCRIBE" else "SUBSCRIBE",
                icon = if(modePro) Icons.Default.Unsubscribe else Icons.Default.Subscriptions,
                isExpanded = expandedSection == SettingsSection.SUBSCRIBE_UNSUBSCRIBE,
                onExpandClick = {
                    expandedSection = if(expandedSection == SettingsSection.SUBSCRIBE_UNSUBSCRIBE) {
                        SettingsSection.NONE
                    } else {
                        SettingsSection.SUBSCRIBE_UNSUBSCRIBE
                    }
                }
            ){
                Column {
                    BodyLargeText(
                        text = if(modePro) "Unsubscribing means that You will be able still use this app but without some important features. Are You sure You want to do this?" else "Subscribing means You will gain access to additional features this app offers. Subscription is not for free, but it's worth it.",
                        modifier = Modifier
                            .padding(start = 10.dp, top = 5.dp, bottom = 10.dp, end = 10.dp)
                    )
                    CustomButton(
                        text = if(modePro) "UNSUBSCRIBE" else "SUBSCRIBE",
                        modifier = Modifier
                            .fillMaxSize(),
                        onClick = {
                            if(!modePro){
                                Qonversion.shared.purchase(
                                    context.findActivity()!!,
                                    viewModel.offerings.find { it.offeringId == "test" }?.products?.firstOrNull()!!,
                                    object: QonversionEntitlementsCallback {
                                        override fun onError(error: QonversionError) {
                                            Toast.makeText(context, "Error: ${error.description}", Toast.LENGTH_LONG).show()
                                            viewModel.updatePermissions(context)
                                        }

                                        override fun onSuccess(entitlements: Map<String, QEntitlement>) {
                                            android.util.Log.d("Qonversion", "Success: ${entitlements.keys}")

                                            val premiumEntitlement = entitlements["test"]

                                            val anyActive = entitlements.values.any { it.isActive }

                                            if (premiumEntitlement?.isActive == true || anyActive) {
                                                val b = 1
                                                viewModel.updatePermissions(context)
                                                Toast.makeText(context, "Subscription successful", Toast.LENGTH_LONG).show()
                                            }
                                            else{
                                                // If purchase worked but entitlement is missing, it might be a configuration delay.
                                                // Force a permission update anyway to re-fetch from server.
                                                viewModel.updatePermissions(context)
                                                Toast.makeText(context, "Purchase complete. Updating status...", Toast.LENGTH_SHORT).show()
                                            }
                                        }

                                    }

                                )
                            }
                            else{

                                viewModel.updatePermissions(context)
                                // UNSUBSCRIBE LOGIC
                                // We must open the Google Play Store to the specific subscription page
                                try {
                                    val intent = android.content.Intent(
                                        android.content.Intent.ACTION_VIEW,
                                        "https://play.google.com/store/account/subscriptions?sku=Plus&package=${context.packageName}".toUri()
                                    )
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    // Fallback if Play Store app is not available
                                    val intent = android.content.Intent(
                                        android.content.Intent.ACTION_VIEW,
                                        "https://play.google.com/store/account/subscriptions".toUri()
                                    )
                                    context.startActivity(intent)
                                }

   //                             Toast.makeText(context, "You are already subscribed", Toast.LENGTH_LONG).show()
                            }

                        }
                    )
                }
            }

            ExpandableCard(
                title = "CLIENTS",
                icon = Icons.Default.Person,
                isExpanded = expandedSection == SettingsSection.CLIENTS,
                onExpandClick = {
                    expandedSection = if(expandedSection == SettingsSection.CLIENTS) {
                        SettingsSection.NONE
                    } else {
                        SettingsSection.CLIENTS
                    }
                }
            ) {
                Column{
                    BodyLargeText(
                        text = if(clientsInUse.isNullOrEmpty()){
                            "You don't have any clients yet, press button below to add new client to your database"
                        } else {
                            if(clientsInUse.size == 1)
                                "You have ${clientsInUse.size} client in your database, press button below to navigate to it"
                            else
                                "You have ${clientsInUse.size} clients in your database, press button below to navigate to it"
                        },
                        modifier = Modifier
                            .padding(start = 10.dp, top = 5.dp, bottom = 10.dp, end = 10.dp)
                    )
                    CustomButton(
                        modifier = Modifier
                            .fillMaxSize(),
                        text = "CLIENT DATABASE",
                        onClick = {
                            navController.navigate(InvoiceCreatorScreen.ChooseClientV2.name)
                        }
                    )
                }


            }

            ExpandableCard(
                title = "ITEMS",
                icon = Icons.Default.Checklist,
                isExpanded = expandedSection == SettingsSection.ITEMS,
                onExpandClick = {
                    expandedSection = if(expandedSection == SettingsSection.ITEMS) {
                        SettingsSection.NONE
                    } else {
                        SettingsSection.ITEMS
                    }
                }
            ) {
                Column{
                    BodyLargeText(
                        text = if(itemListInUse.isNullOrEmpty()){
                            "You don't have any items yet, press button below to add new item to your database"
                        } else {
                            if(itemListInUse.size == 1)
                                "You have ${itemListInUse.size} item in your database, press button below to navigate to it"
                            else
                                "You have ${itemListInUse.size} items in your database, press button below to navigate to it"
                        },
                        modifier = Modifier
                            .padding(start = 10.dp, top = 5.dp, bottom = 10.dp, end = 10.dp)
                    )
                    CustomButton(
                        modifier = Modifier
                            .fillMaxSize(),
                        text = "ITEMS DATABASE",
                        onClick = {
                            navController.navigate(InvoiceCreatorScreen.ChooseItemV2.name)
                        }
                    )
                }
            }
        }

        if (loadingStatus.value != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = MaterialTheme.myColors.material.primary)
                    Spacer(modifier = Modifier.height(16.dp))
                    BodyLargeText(
                        text = loadingStatus.value ?: "",
                        color = Color.White,
                    )
                }
            }
        }
    }


    if (alertDialogUpdateUser.value) {
        val a =1
        AlertDialogAddMainUser(
            title = "EDIT USER",
            user = user,
            closeAlertDialog = {
                alertDialogUpdateUser.value = false
            },
            canBeDismissed = true
        )
    }
}


fun android.content.Context.findActivity(): android.app.Activity? {
    var context = this
    while (context is android.content.ContextWrapper) {
        if (context is android.app.Activity) return context
        context = context.baseContext
    }
    return null
}

//todo add upgrade button !!!
// todo go to client base to edit
// todo go to item base to edit
// todo edit item from add invoice screen, change it to alert dialog :)