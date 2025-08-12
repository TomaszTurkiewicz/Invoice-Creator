package com.tt.invoicecreator

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.ui.app_bar.TopAppBarWithAction
import com.tt.invoicecreator.ui.screens.AddClientScreen
import com.tt.invoicecreator.ui.screens.AddInvoiceScreen
import com.tt.invoicecreator.ui.screens.AddInvoiceV2
import com.tt.invoicecreator.ui.screens.AddItemScreen
import com.tt.invoicecreator.ui.screens.ChooseClientScreen
import com.tt.invoicecreator.ui.screens.ChooseItemScreen
import com.tt.invoicecreator.ui.screens.ChooseModeScreen
import com.tt.invoicecreator.ui.screens.InvoicesScreen
import com.tt.invoicecreator.ui.screens.InvoicesScreenV2
import com.tt.invoicecreator.viewmodel.AppViewModel

@Composable
fun InvoiceCreatorApp (
    viewModel: AppViewModel,
    navController: NavHostController = rememberNavController()
){

    val context = LocalContext.current

    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = InvoiceCreatorScreen.valueOf(
        backStackEntry?.destination?.route ?: InvoiceCreatorScreen.ChooseMode.name
    )

    var appBarState by remember {
        mutableStateOf(AppBarState())
    }

    Scaffold (
        topBar = {
            TopAppBarWithAction(
                appBarState = appBarState,
                currentScreen = currentScreen,
                context = context
            )

        }
    ) {
        innerPadding ->
        NavHost(
            navController = navController,
            startDestination = InvoiceCreatorScreen.ChooseMode.name,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(route = InvoiceCreatorScreen.Invoices.name) {
                InvoicesScreen(
                    viewModel = viewModel,
                    ignoredOnComposing = {
                        appBarState = it
                    },
                    navController = navController
                )
            }

            composable(route = InvoiceCreatorScreen.AddInvoice.name) {
                AddInvoiceScreen(
                    viewModel = viewModel,
                    ignoredOnComposing = {
                        appBarState = it
                    },
                    navController = navController
                )
            }

            composable(route = InvoiceCreatorScreen.ChooseClient.name) {
                ChooseClientScreen(
                    viewModel = viewModel,
                    ignoredOnComposing = {
                        appBarState = it
                    },
                    navController = navController
                )
            }

            composable(route = InvoiceCreatorScreen.AddClient.name) {
                AddClientScreen(
                    viewModel = viewModel,
                    ignoredOnComposing = {
                        appBarState = it
                    },
                    navController = navController
                )
            }

            composable(route = InvoiceCreatorScreen.ChooseItem.name) {
                ChooseItemScreen(
                    viewModel = viewModel,
                    ignoredOnComposing = {
                        appBarState = it
                    },
                    navController = navController
                )
            }

            composable(route = InvoiceCreatorScreen.AddItem.name) {
                AddItemScreen(
                    viewModel = viewModel,
                    ignoredOnComposing = {
                        appBarState = it
                    },
                    navController = navController
                )
            }

            composable(route = InvoiceCreatorScreen.ChooseMode.name) {
                ChooseModeScreen(
                    navController = navController
                )
            }

            composable(route = InvoiceCreatorScreen.InvoicesV2.name) {
                InvoicesScreenV2(
                    viewModel = viewModel,
                    ignoredOnComposing = {
                        appBarState = it
                    },
                    navController = navController
                )
            }

            composable(route = InvoiceCreatorScreen.AddInvoiceV2.name) {
                AddInvoiceV2()
            }

        }
    }


}