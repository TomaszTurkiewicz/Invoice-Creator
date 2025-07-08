package com.tt.invoicecreator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tt.invoicecreator.viewmodel.AppViewModel

@Composable
fun InvoiceCreatorApp (
    viewModel: AppViewModel,
    navController: NavHostController = rememberNavController()
){

    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = InvoiceCreatorScreen.valueOf(
        backStackEntry?.destination?.route ?: InvoiceCreatorScreen.Welcome.name
    )


}