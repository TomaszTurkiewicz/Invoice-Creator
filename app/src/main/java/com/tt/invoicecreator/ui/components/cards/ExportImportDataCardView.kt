package com.tt.invoicecreator.ui.components.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseUser
import com.tt.invoicecreator.ui.components.CustomButton
import com.tt.invoicecreator.ui.components.texts.BodyLargeText
import com.tt.invoicecreator.ui.components.texts.TitleLargeText

@Composable
fun ExportImportDataCardView(
    modePro: Boolean,
    firebaseUser: FirebaseUser?,
    onDeleteAccountClicked: () -> Unit,
    onLinkAccountClicked: () -> Unit,
    onExportClick: () -> Unit,
    onImportClicked: () -> Unit
) {

        Column {
            TitleLargeText(
                text = "CLOUD BACKUP & SYNC",
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            val description = if(modePro){
                "Your data is backed up to your private cloud storage using your Google account. " +
                        "Click EXPORT to upload. On your new device, click IMPORT to restore your data. " +
                        "The cloud copy is automatically deleted after a successful import."
            }
            else{
                "Exporting and importing data is a Pro feature. Upgrade to use secure Cloud Sync with your Google account."
            }
            BodyLargeText(
                text = description,
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .fillMaxWidth()
            )
            if(modePro){
                if(firebaseUser == null){
                    BodyLargeText(
                        text = "To use Cloud Sync, please link Google Account first.",
                        modifier = Modifier.padding(bottom = 10.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                    CustomButton(
                        text = "LINK GOOGLE ACCOUNT",
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onLinkAccountClicked()}
                    )
                } else{
                    // PRO and Linked - Show real actions
                    BodyLargeText(
                        text = "Linked as: ${firebaseUser.email}",
                        modifier = Modifier.padding(bottom = 15.dp).fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    BodyLargeText(
                        text = "Warning: Importing will remove any existing data on this device.",
                        modifier = Modifier.padding(bottom = 20.dp).fillMaxWidth(),
                        color = MaterialTheme.colorScheme.error
                    )

                    CustomButton(
                        text = "EXPORT DATA (UPLOAD)",
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {onExportClick()}
                    )

                    CustomButton(
                        text = "IMPORT DATA (RESTORE)",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        onClick = {onImportClicked()}
                    )
                    CustomButton(
                        text = "DELETE CLOUD DATA & UNLINK",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        makeItWarning = true,
                        onClick = {
                            onDeleteAccountClicked()
                        }
                    )
                }
            }else{
                BodyLargeText(
                    text = "You have to be an active subscriber to use this feature.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
