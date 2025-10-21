package com.tt.invoicecreator.ui.screens

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.room.util.TableInfo
import com.tt.invoicecreator.InvoiceCreatorScreen
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.data.SharedPreferences
import com.tt.invoicecreator.ui.alert_dialogs.AlertDialogAddMainUser
import com.tt.invoicecreator.ui.components.ClientCardViewV2

@Composable
fun Settings(
    ignoredOnComposing: (AppBarState) -> Unit
) {
    val context = LocalContext.current
    val user = SharedPreferences.readUserDetails(context)
    LaunchedEffect(key1 = true) {
        ignoredOnComposing(
            AppBarState(
                title = "SETTINGS",
                action = null
            )
        )
    }

    val alertDialogUpdateUser = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
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
    }

    if(alertDialogUpdateUser.value){
        AlertDialogAddMainUser(
            user = user,
            closeAlertDialog = {
                alertDialogUpdateUser.value = false
            },
            canBeDismissed = true
        )
    }
}