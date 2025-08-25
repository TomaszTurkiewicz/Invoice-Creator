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
            (this.application as InvoiceCreatorApplication).databaseV2.getItemDaoV2(),
            (this.application as InvoiceCreatorApplication).databaseV2.getClientDaoV2(),
            (this.application as InvoiceCreatorApplication).databaseV2.getInvoiceItemDaoV2(),
            (this.application as InvoiceCreatorApplication).databaseV2.getInvoiceDaoV2(),
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InvoiceCreatorTheme {
                InvoiceCreatorApp(
                    viewModel = viewModel
                )
            }
        }
    }
}

/**
 * todo change one item to pro condition
 * todo add invoice paid if PRO
 * todo add changing color schema if PRO
 * todo add advert to add invoice if not PRO
 * todo add settings screen
 * **/