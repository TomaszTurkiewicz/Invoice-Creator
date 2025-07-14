package com.tt.invoicecreator

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.ui.app_bar.TopAppBarWithAction
import com.tt.invoicecreator.ui.screens.AddItemScreen
import com.tt.invoicecreator.ui.screens.InvoicesScreen
import com.tt.invoicecreator.viewmodel.AppViewModel

@Composable
fun InvoiceCreatorApp (
    viewModel: AppViewModel,
    navController: NavHostController = rememberNavController()
){

    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = InvoiceCreatorScreen.valueOf(
        backStackEntry?.destination?.route ?: InvoiceCreatorScreen.Invoices.name
    )

    var appBarState by remember {
        mutableStateOf(AppBarState())
    }

    Scaffold (
        topBar = {
            TopAppBarWithAction(
                appBarState = appBarState,
                currentScreen = currentScreen
            )

        }
    ) {
        innerPadding ->
        NavHost(
            navController = navController,
            startDestination = InvoiceCreatorScreen.Invoices.name,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(route = InvoiceCreatorScreen.Invoices.name) {
                InvoicesScreen(
                    viewModel = viewModel,
                    ignoredOnComposing = {
                        appBarState = it
                    }
                )
            }

            composable(route = InvoiceCreatorScreen.AddItem.name) {
                AddItemScreen(
                    viewModel = viewModel,
                    ignoredOnComposing = {
                        appBarState = it
                    }
                )
            }
        }
    }


}