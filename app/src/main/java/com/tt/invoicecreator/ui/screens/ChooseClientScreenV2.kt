package com.tt.invoicecreator.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.tt.invoicecreator.InvoiceCreatorScreen
import com.tt.invoicecreator.R
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.data.roomV2.entities.ClientV2
import com.tt.invoicecreator.helpers.FilterClients
import com.tt.invoicecreator.ui.alert_dialogs.AlertDialogFourInputTextsWithLabelAndTwoButtons
import com.tt.invoicecreator.ui.components.ListOfClientsV2
import com.tt.invoicecreator.ui.components.texts.TitleLargeText
import com.tt.invoicecreator.viewmodel.AppViewModel

@Composable
fun ChooseClientScreenV2(
    viewModel: AppViewModel,
    ignoredOnComposing: (AppBarState) -> Unit,
    navController: NavController,
    clientList:  List<ClientV2>?,
    onClientChosenClick: (ClientV2) -> Unit
) {

    val addClientAlertDialog = remember {
        mutableStateOf(false)
    }

    val editClientAlertDialog = remember {
        mutableStateOf(false)
    }

    val tempClient = remember {
        mutableStateOf(ClientV2())
    }

    val clientsInUse = FilterClients.getInUse(clientList)


    LaunchedEffect(key1 = true) {
        ignoredOnComposing(
            AppBarState(
                title = "CHOOSE CLIENT",
                action = {
                    Row {
                        IconButton(onClick = {
                            addClientAlertDialog.value = true
                        }) {
                            Icon(painter = painterResource(R.drawable.baseline_add_24), null)
                        }
                        IconButton(onClick = {
                            navController.navigate(InvoiceCreatorScreen.Settings.name)
                        }) {
                            Icon(painter = painterResource(R.drawable.baseline_settings_24), null)
                        }
                    }

                }
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center)
    {
        if(clientsInUse.isNullOrEmpty()){
            TitleLargeText(
                text = "press + on top of the app"
            )
        }
        else{
            ListOfClientsV2(
                list = clientsInUse,
                clientChosen = {
                    onClientChosenClick(it)
                },
                editClient = {
                    tempClient.value = it
                    editClientAlertDialog.value = true
                }
            )
        }
    }

    if(editClientAlertDialog.value){
        AlertDialogFourInputTextsWithLabelAndTwoButtons(
            title = "EDIT CLIENT",
            labelOne = "Client name",
            inputOne = tempClient.value.clientName,
            labelTwo = "Client address line 1",
            inputTwo = tempClient.value.clientAddress1,
            labelThree = "Client address line 2",
            inputThree = tempClient.value.clientAddress2,
            labelFour = "Client city",
            inputFour = tempClient.value.clientCity,
            buttonTextOne = "UPDATE",
            buttonTextTwo = "DELETE",
            secondButtonEnabled = true,
            onDismissRequest = {
                editClientAlertDialog.value = false
            },
            firstButtonAction = { firstLine, secondLine, thirdLine, fourthLine ->
//                tempClient.value.copy(
//                    clientInUse = false
//                )

                viewModel.updateClient(tempClient.value.copy(clientInUse = false))
                viewModel.saveClientV2(
                    ClientV2(
                        clientName = firstLine,
                        clientAddress1 = secondLine,
                        clientAddress2 = thirdLine,
                        clientCity = fourthLine
                    )
                )
            },
            secondButtonAction = {
                tempClient.value.copy(
                    clientInUse = false
                )
                viewModel.updateClient(tempClient.value.copy(clientInUse = false))
            }
        )
    }

    if(addClientAlertDialog.value){
        AlertDialogFourInputTextsWithLabelAndTwoButtons(
            title = "ADD NEW CLIENT",
            labelOne = "Client name",
            labelTwo = "Client address line 1",
            labelThree = "Client address line 2",
            labelFour = "Client city",
            buttonTextOne = "SAVE",
            secondButtonEnabled = false,
            onDismissRequest = {
                addClientAlertDialog.value = false
            },
            firstButtonAction = { firstLine, secondLine, thirdLine, fourthLine ->
                viewModel.saveClientV2(
                    ClientV2(
                        clientName = firstLine,
                        clientAddress1 = secondLine,
                        clientAddress2 = thirdLine,
                        clientCity = fourthLine
                )
                )
            }
        ) {
            // do nothing
        }
    }
}