package com.tt.invoicecreator.ui.app_bar


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.ui.theme.myColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithAction(
    appBarState: AppBarState
) {    TopAppBar(
    title = {
        Text(
            text = appBarState.title,
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
    colors = TopAppBarDefaults.mediumTopAppBarColors(
        containerColor = MaterialTheme.myColors.primaryLight,
        titleContentColor = MaterialTheme.myColors.primaryDark,
        actionIconContentColor = MaterialTheme.myColors.primaryDark
    ),
    modifier = Modifier.fillMaxWidth()
)

}