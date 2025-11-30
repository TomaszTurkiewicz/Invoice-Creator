package com.tt.invoicecreator.ui.components.cards

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.roomV2.backups.BackupManager
import com.tt.invoicecreator.ui.components.CustomButton
import com.tt.invoicecreator.ui.components.texts.BodyLargeText
import com.tt.invoicecreator.ui.components.texts.TitleLargeText
import com.tt.invoicecreator.viewmodel.AppViewModel

@Composable
fun ExportImportData(
    modePro: Boolean,
    context: Context,
    viewModel: AppViewModel,
    importLauncher: ManagedActivityResultLauncher<Array<String>, Uri?>
) {
    CustomCardView(

    ) {
        Column {
            TitleLargeText(
                text = "CHANGING DEVICE?",
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            BodyLargeText(
                text = "You can export and import your entire database. Click EXPORT to create BACKUP file. Send it to Your new device. Install app on Your new device, go to settings and hit IMPORT button. Locate BACKUP file and select it. All data will be restored.",
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .fillMaxWidth()
            )
            if(modePro){
                BodyLargeText(
                    text = "When installing this app at Your new device make sure to import database before creating any new client or invoice. Importing BACKUP file will remove any existing data.",
                    modifier = Modifier
                        .padding(bottom = 30.dp)
                        .fillMaxWidth(),
                    color = MaterialTheme.colorScheme.error
                )

                CustomButton(
                    text = "EXPORT DATA (BACKUP)",
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        BackupManager.exportDatabaseToJson(context, viewModel)
                    }
                )

                CustomButton(
                    text = "IMPORT DATA (RESTORE)",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp),
                    onClick = {
                        importLauncher.launch(arrayOf("application/json"))
                    }
                )
            }
            else{
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
}