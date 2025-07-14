package com.tt.invoicecreator.ui.app_bar

import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.InvoiceCreatorScreen
import com.tt.invoicecreator.data.AppBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithAction(
    appBarState: AppBarState,
    currentScreen: InvoiceCreatorScreen
) {
    TopAppBar(
        title = {
            Text(
                text = when(currentScreen){
                    InvoiceCreatorScreen.Invoices -> currentScreen.name
                },
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp),
                textAlign = TextAlign.Center
            )
        },
        actions = {
            appBarState.action?.invoke(this)
        },
        modifier = Modifier.fillMaxWidth()
    )
}