package com.tt.invoicecreator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.tt.invoicecreator.ui.theme.InvoiceCreatorTheme
import com.tt.invoicecreator.viewmodel.AppViewModel
import com.tt.invoicecreator.viewmodel.AppViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel:AppViewModel by viewModels {
        AppViewModelFactory(
            (this.application as InvoiceCreatorApplication).database.getItemDao(),
            (this.application as InvoiceCreatorApplication).database.getClientDao(),
            (this.application as InvoiceCreatorApplication).database.getInvoiceDao()
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