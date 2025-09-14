package com.tt.invoicecreator.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.tt.invoicecreator.InvoiceCreatorScreen
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.data.SharedPreferences
import com.tt.invoicecreator.data.SignatureFile
import com.tt.invoicecreator.helpers.InvoiceDueDate
import com.tt.invoicecreator.helpers.InvoiceNumber
import com.tt.invoicecreator.ui.alert_dialogs.AlertDialogInvoiceNumberV2
import com.tt.invoicecreator.ui.alert_dialogs.AlertDialogPaymentMethod
import com.tt.invoicecreator.ui.alert_dialogs.AlertDialogSignature
import com.tt.invoicecreator.ui.components.ClientCardViewV2
import com.tt.invoicecreator.ui.components.EmptyItemCardViewV2
import com.tt.invoicecreator.ui.components.InvoiceNumberCardView
import com.tt.invoicecreator.ui.components.ItemCardViewV2
import com.tt.invoicecreator.ui.components.PaymentMethodCardView
import com.tt.invoicecreator.ui.components.SignatureCardView
import com.tt.invoicecreator.viewmodel.AppViewModel
import java.io.File

@Composable
fun AddInvoiceScreenV2(
    viewModel: AppViewModel,
    ignoredOnComposing: (AppBarState) -> Unit,
    navController: NavController,
    modePro:Boolean
) {
    val invoiceList by viewModel.invoiceListV2.observeAsState()


    val itemInvoiceList = viewModel.getInvoiceItemList()

    val time = remember {
        mutableLongStateOf(0L)
    }

    val invoiceNumberAlertDialog = remember {
        mutableStateOf(false)
    }

    val invoicePaymentMethodAlertDialog = remember {
        mutableStateOf(false)
    }

    val invoiceSignatureAlertDialog = remember {
        mutableStateOf(false)
    }

    val image = remember {
        mutableStateOf<Bitmap?>(null)
    }

    val context = LocalContext.current
    viewModel.paymentMethod = SharedPreferences.readPaymentMethod(context) ?: ""

    LaunchedEffect(key1 = true) {
        ignoredOnComposing(
            AppBarState(
                action = {
                    Row {
                        IconButton(onClick = {
                            navController.navigate(InvoiceCreatorScreen.Settings.name)
                        }) {
                            Icon(Icons.Default.Settings,null)
                        }
                    }

                }
            )
        )
    }

    LaunchedEffect(key1 = true) {
        viewModel.cleanAdFlags()
    }

    LaunchedEffect(key1 = true) {
        time.longValue = System.currentTimeMillis()
        viewModel.getInvoiceV2().time = time.longValue

        if (viewModel.calculateNumber) {
            viewModel.getInvoiceV2().invoiceNumber = InvoiceNumber.getNewNumberV2(viewModel.getInvoiceV2().time,invoiceList)
        }

        if(viewModel.calculateDueDate && modePro){
            viewModel.getInvoiceV2().dueDate = InvoiceDueDate.getDueDate(time.longValue)
        }
    }

    val pic = SignatureFile.getFilePath(context).toUri().path
    val file = pic?.let {
        File(it)
    }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ){
            InvoiceNumberCardView(
                number = viewModel.getInvoiceV2().invoiceNumber,
                time = time.longValue,
                modePro = modePro,
                dueDate = viewModel.getInvoiceV2().dueDate
            ){
                invoiceNumberAlertDialog.value = true
            }
            ClientCardViewV2(
                clientName = viewModel.getInvoiceV2().client.clientName,
                clientAddressLine1 = viewModel.getInvoiceV2().client.clientAddress1,
                clientAddressLine2 = viewModel.getInvoiceV2().client.clientAddress2,
                clientCity = viewModel.getInvoiceV2().client.clientCity,
                showCardView = {
                    viewModel.getInvoiceV2().client.clientName != ""
                },
                onClick = {
                    navController.navigate(InvoiceCreatorScreen.ChooseClientV2.name)
                }
            )
            itemInvoiceList.forEach{ itemInvoice ->
                ItemCardViewV2(
                    itemInvoice
                )
            }

            if(!modePro){
                if(itemInvoiceList.isEmpty()){
                    EmptyItemCardViewV2(
                        position = 0,
                        showPosition = false,
                        onClick = {
                            navController.navigate(InvoiceCreatorScreen.ChooseItemV2.name)
                        }
                    )
                }
            }else{
                if(itemInvoiceList.size<10){
                EmptyItemCardViewV2(
                    position = if(itemInvoiceList.isEmpty()) 1 else itemInvoiceList.size+1,
                    showPosition = true,
                    onClick = {
                        navController.navigate(InvoiceCreatorScreen.ChooseItemV2.name)
                    }
                )
                }
            }

            PaymentMethodCardView(
                onClick = {
                    invoicePaymentMethodAlertDialog.value = true
                },
                paymentMethod = viewModel.paymentMethod
            )

            SignatureCardView(
                onClick = {
                    invoiceSignatureAlertDialog.value = true
                },
                imageBitmap = if(file!!.exists()){
                    val options = BitmapFactory.Options()
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888
                    image.value = BitmapFactory.decodeFile(file.path,options)
                    image.value
                }else{
                    null
                }
            )
            Button(
                enabled = viewModel.getInvoiceV2().client.clientName!="" && viewModel.getInvoiceItemList()
                    .isNotEmpty(),
                onClick ={
                    viewModel.saveInvoiceV2()
                    navController.navigateUp()
                },
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "SAVE")
            }
        }

    if(invoiceNumberAlertDialog.value){
        AlertDialogInvoiceNumberV2(
            onDismissRequest = {
                invoiceNumberAlertDialog.value = false
            },
            viewModel = viewModel,
            modePro = modePro
        )
    }

    if(invoicePaymentMethodAlertDialog.value){
        AlertDialogPaymentMethod (
            onDismissRequest = {
                invoicePaymentMethodAlertDialog.value = false
            }
        )
    }

    if(invoiceSignatureAlertDialog.value){
        AlertDialogSignature (
            onDismissRequest = {
                invoiceSignatureAlertDialog.value = false
            }
        )
    }
}