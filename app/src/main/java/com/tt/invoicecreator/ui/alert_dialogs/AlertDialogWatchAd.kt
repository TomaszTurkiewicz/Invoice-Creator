package com.tt.invoicecreator.ui.alert_dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.ui.components.CustomButton
import com.tt.invoicecreator.ui.components.CustomCardView
import com.tt.invoicecreator.ui.components.texts.BodyLargeText
import com.tt.invoicecreator.ui.components.texts.TitleLargeText

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
                TitleLargeText(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    text = "WATCH ADD"
                )
                BodyLargeText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    text = "This app is free to use. You don't have to pay anything. So please watch add to be able to add new invoice. Press button below WATCH ADD when it gets available.\nYou can also upgrade app to PRO version. PRO version has few features more. Such as: no need to watch adds, payment history, multiple items in one invoice, etc. Go to SETTINGS, press UPGRADE APP button and follow instruction if you wish to do it."
                )
                CustomButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    onClick = {
                        watchAdClicked()
                    },
                    enabled = adLoaded,
                    text = "WATCH ADD"
                )
            }
        }
    }
}
