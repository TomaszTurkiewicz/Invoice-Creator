package com.tt.invoicecreator.ui.alert_dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tt.invoicecreator.ui.components.CustomCardView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogWatchAd(
    onDismissRequest: () -> Unit,
    adLoaded:Boolean,
    watchAdClicked: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = {
            onDismissRequest()
        }
    ) {
        CustomCardView {
            Column(
                modifier = Modifier
            )
            {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    text = "TITLE"
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    text = "YOU MUST WATCH AD TO USE THIS FEATURE"
                )
                Button(
                    onClick = {
                        watchAdClicked()
                    },
                    enabled = adLoaded
                ) {
                    Text(text = "WATCH AD")
                }
            }
        }
    }
}
