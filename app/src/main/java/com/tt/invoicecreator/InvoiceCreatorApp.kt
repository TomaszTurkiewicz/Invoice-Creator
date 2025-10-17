package com.tt.invoicecreator

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.tt.invoicecreator.ui.app_bar.TopAppBarWithCustomTitleAndAction
import com.tt.invoicecreator.ui.screens.AddClientScreenV2
import com.tt.invoicecreator.ui.screens.AddInvoiceScreenV2
import com.tt.invoicecreator.ui.screens.AddItemScreenV2
import com.tt.invoicecreator.ui.screens.ChooseClientScreenV2
import com.tt.invoicecreator.ui.screens.ChooseItemScreenV2
import com.tt.invoicecreator.ui.screens.ChooseModeScreen
import com.tt.invoicecreator.ui.screens.FilteredInvoicesScreen
import com.tt.invoicecreator.ui.screens.InvoiceInfoScreenV2
import com.tt.invoicecreator.ui.screens.InvoicesScreenV2
import com.tt.invoicecreator.ui.screens.Settings
import com.tt.invoicecreator.viewmodel.AppViewModel

@Composable
fun InvoiceCreatorApp (
    viewModel: AppViewModel,
    navController: NavHostController = rememberNavController(),
    activity: MainActivity
){

    val context = LocalContext.current

    val backStackEntry by navController.currentBackStackEntryAsState()

    val invoiceListV2 by viewModel.invoiceListV2.observeAsState()

    val currentScreen = InvoiceCreatorScreen.valueOf(
        backStackEntry?.destination?.route ?: InvoiceCreatorScreen.ChooseMode.name
    )

    var appBarState by remember {
        mutableStateOf(AppBarState())
    }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold (

        topBar = {
            when(currentScreen){
                InvoiceCreatorScreen.FilteredInvoicesV2 -> {
                    TopAppBarWithCustomTitleAndAction(
                        appBarState = appBarState,
                        invoiceStatus = uiState.invoiceState
                    )
                }
                else -> {
                    TopAppBarWithAction(
                        appBarState = appBarState,
                        currentScreen = currentScreen,
                        context = context
                    )
                }
            }
        }
    ) {
        innerPadding ->



        NavHost(
            navController = navController,
            startDestination = InvoiceCreatorScreen.ChooseMode.name,
            modifier = Modifier.padding(innerPadding)
        ){

            composable(route = InvoiceCreatorScreen.ChooseItemV2.name) {
                ChooseItemScreenV2(
                    viewModel = viewModel,
                    ignoredOnComposing = {
                        appBarState = it
                    },
                    navController = navController
                )
            }

            composable(route = InvoiceCreatorScreen.AddItemV2.name) {
                AddItemScreenV2(
                    viewModel = viewModel,
                    ignoredOnComposing = {
                        appBarState = it
                    },
                    navController = navController
                )
            }

            composable(route = InvoiceCreatorScreen.ChooseMode.name) {
                ChooseModeScreen(
                    viewModel = viewModel,
                    ignoredOnComposing = {
                        appBarState = it
                    },
                    navController = navController
                )
            }

            composable(route = InvoiceCreatorScreen.InvoicesV2.name) {
                InvoicesScreenV2(
                    viewModel = viewModel,
                    ignoredOnComposing = {
                        appBarState = it
                    },
                    navController = navController,
                    modePro = uiState.modePro,
                    adLoaded = uiState.rewardedAppLoaded,
                    activity = activity,
                    adWatched = uiState.rewardedAdWatched,
                    invoiceStatus = uiState.invoiceState
                )
            }

            composable(route = InvoiceCreatorScreen.AddInvoiceV2.name) {
                AddInvoiceScreenV2(
                    viewModel = viewModel,
                    ignoredOnComposing = {
                        appBarState = it
                    },
                    navController = navController,
                    modePro = uiState.modePro,
                    invoiceList = invoiceListV2
                )
            }

            composable(route = InvoiceCreatorScreen.ChooseClientV2.name) {
                ChooseClientScreenV2(
                    viewModel = viewModel,
                    ignoredOnComposing = {
                        appBarState = it
                    },
                    navController = navController
                )
            }

            composable(route = InvoiceCreatorScreen.AddClientV2.name) {
                AddClientScreenV2(
                    viewModel = viewModel,
                    ignoredOnComposing = {
                        appBarState = it
                    },
                    navController = navController
                )
            }

            composable(route = InvoiceCreatorScreen.Settings.name) {
                Settings(
                    ignoredOnComposing = {
                        appBarState = it
                    }
                )
            }

            composable(route = InvoiceCreatorScreen.InvoiceInfoV2.name) {
                InvoiceInfoScreenV2(
                    invoiceV2 = uiState.invoiceV2,
                    invoiceItemListV2 = uiState.invoiceItemListV2,
                    paidListV2 = uiState.paidListV2,
                    ignoredOnComposing = {
                        appBarState = it
                    },
                    viewModel = viewModel,
                    uiState= uiState,
                    navController = navController
                )
            }

            composable(route = InvoiceCreatorScreen.FilteredInvoicesV2.name) {
                FilteredInvoicesScreen(
                    viewModel = viewModel,
                    ignoredOnComposing = {
                        appBarState = it
                    },
                    navController = navController,
                    invoiceStatus = uiState.invoiceState,
                    modePro = uiState.modePro
                )

            }

        }
    }


}