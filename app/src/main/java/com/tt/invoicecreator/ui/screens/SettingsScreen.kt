package com.tt.invoicecreator.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.data.SharedPreferences
import com.tt.invoicecreator.data.roomV2.backups.BackupManager
import com.tt.invoicecreator.ui.alert_dialogs.AlertDialogAddMainUser
import com.tt.invoicecreator.ui.components.cards.ClientCardViewV2
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
    viewModel: AppViewModel
) {
    val context = LocalContext.current
    val user = SharedPreferences.readUserDetails(context)
    val scope = rememberCoroutineScope()

    val loadingStatus = remember {
        mutableStateOf<String?>(null)
    }
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
            ClientCardViewV2(
                clientName = user.userName ?: "",
                clientAddressLine1 = user.userAddressLine1 ?: "",
                clientAddressLine2 = user.userAddressLine2 ?: "",
                clientCity = user.userCity ?: "",
                showCardView = {
                    true
                },
                onClick = {
                    alertDialogUpdateUser.value = true
                }
            )

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

//todo add upgrade button !!!