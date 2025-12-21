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
import com.tt.invoicecreator.ui.components.cards.CustomCardView
import com.tt.invoicecreator.ui.components.texts.BodyLargeText
import com.tt.invoicecreator.ui.components.texts.TitleLargeText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogStar(
    onDismissRequest: () -> Unit
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
                    text = "PREMIUM VERSION"
                )
                BodyLargeText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    text = "This STAR represents PREMIUM access to all functionality of the app"
                )
                CustomButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    onClick = {
                        onDismissRequest()
                    },
                    text = "OK"
                )
            }
        }
    }
}