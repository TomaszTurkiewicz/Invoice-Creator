package com.tt.invoicecreator.ui.alert_dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.ui.components.CustomButton
import com.tt.invoicecreator.ui.components.cards.CustomCardView
import com.tt.invoicecreator.ui.components.texts.BodyLargeText
import com.tt.invoicecreator.ui.components.texts.TitleLargeText
import com.tt.invoicecreator.ui.theme.myColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogWarning(
    message: String,
    onDismissRequest: () -> Unit,
    buttonText: String,
    buttonClicked: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = {
            //do nothing
        }
    ){
        CustomCardView {
            Column(
                modifier = Modifier
            ) {
                TitleLargeText(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    text = "WARNING",
                    color = MaterialTheme.myColors.error
                )
                BodyLargeText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    text = message
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CustomButton(
                        modifier = Modifier
                            .weight(1f)
                            .padding(10.dp),
                        onClick = {
                            onDismissRequest()
                        },
                        text = "CANCEL"
                    )

                    CustomButton(
                        modifier = Modifier
                            .weight(1f)
                            .padding(10.dp),
                        onClick = {
                            buttonClicked()
                            onDismissRequest()
                        },
                        text = buttonText,
                        makeItWarning = true
                    )

                }
            }

        }
    }
}