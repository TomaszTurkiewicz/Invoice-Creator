package com.tt.invoicecreator.ui.app_bar


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.ui.alert_dialogs.AlertDialogTitleMessageButton
import com.tt.invoicecreator.ui.theme.myColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithAction(
    appBarState: AppBarState,
    modePro: Boolean = false,
    initializing: Boolean
) {
    val starAlertDialog = remember {
        mutableStateOf(false)
    }

    TopAppBar(
    title = {
        Row(

        ) {
            if(!initializing){
                if(modePro){
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Pro mode",
                        tint = MaterialTheme.myColors.material.primary,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(
                                onClick = {
                                    starAlertDialog.value = true
                                }
                            )
                            .align(Alignment.CenterVertically)
                    )
                }
            }

            Text(
                text = appBarState.title,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
                    .padding(end = 10.dp),
                textAlign = TextAlign.Center
                )
            }
            },
    actions = {
        appBarState.action?.invoke(this)
    },
    colors = TopAppBarDefaults.mediumTopAppBarColors(
        containerColor = MaterialTheme.myColors.primaryLight,
        titleContentColor = MaterialTheme.myColors.primaryDark,
        actionIconContentColor = MaterialTheme.myColors.primaryDark
    ),
    modifier = Modifier
        .fillMaxWidth()
)

    if(starAlertDialog.value){
        AlertDialogTitleMessageButton(
            title = "PREMIUM VERSION",
            message = "This STAR represents PREMIUM access to all functionality of the app",
            buttonText = "OK",
            buttonEnabled = true,
            onDismissRequest = {
                starAlertDialog.value = false
            }
        ) {
            // do nothing, just close the alert dialog
        }
    }
}

