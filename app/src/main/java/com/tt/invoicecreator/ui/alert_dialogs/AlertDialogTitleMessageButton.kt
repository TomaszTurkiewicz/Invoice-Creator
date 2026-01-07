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
fun AlertDialogTitleMessageButton(
    title:String,
    message:String,
    buttonText:String,
    onDismissRequest: () -> Unit,
    buttonEnabled:Boolean,
    buttonClicked: () -> Unit
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
                    text = title
                )
                BodyLargeText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    text = message
                )
                CustomButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    onClick = {
                        buttonClicked()
                        onDismissRequest()
                    },
                    enabled = buttonEnabled,
                    text = buttonText
                )
            }
        }
    }
}

//todo przerobic na uniwersalny aleret dialog z title, message and button
