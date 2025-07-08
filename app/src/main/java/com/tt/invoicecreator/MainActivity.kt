package com.tt.invoicecreator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tt.invoicecreator.ui.theme.InvoiceCreatorTheme
import com.tt.invoicecreator.viewmodel.AppViewModel
import com.tt.invoicecreator.viewmodel.AppViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel:AppViewModel by viewModels {
        AppViewModelFactory(
            (this.application as InvoiceCreatorApplication).database.getItemDao()
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            InvoiceCreatorTheme {
                InvoiceCreatorApp(
                    viewModel = viewModel
                )
            }
        }
    }
}