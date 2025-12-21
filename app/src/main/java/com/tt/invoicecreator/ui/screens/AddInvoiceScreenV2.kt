package com.tt.invoicecreator.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.tt.invoicecreator.InvoiceCreatorScreen
import com.tt.invoicecreator.R
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.data.SharedPreferences
import com.tt.invoicecreator.data.SignatureFile
import com.tt.invoicecreator.data.roomV2.entities.InvoiceV2
import com.tt.invoicecreator.helpers.AddInvoiceSection
import com.tt.invoicecreator.helpers.DateAndTime
import com.tt.invoicecreator.helpers.InvoiceDueDate
import com.tt.invoicecreator.helpers.InvoiceNumber
import com.tt.invoicecreator.helpers.PdfUtilsV2
import com.tt.invoicecreator.ui.alert_dialogs.AlertDialogInvoiceNumberV2
import com.tt.invoicecreator.ui.alert_dialogs.AlertDialogPaymentMethod
import com.tt.invoicecreator.ui.alert_dialogs.AlertDialogSignature
import com.tt.invoicecreator.ui.components.CustomButton
import com.tt.invoicecreator.ui.components.ExpandableWithCompletedIconCard
import com.tt.invoicecreator.ui.components.cards.EmptyItemCardViewV2
import com.tt.invoicecreator.ui.components.cards.ItemCardViewV2
import com.tt.invoicecreator.ui.components.texts.BodyLargeText
import com.tt.invoicecreator.viewmodel.AppViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun AddInvoiceScreenV2(
    viewModel: AppViewModel,
    ignoredOnComposing: (AppBarState) -> Unit,
    navController: NavController,
    modePro:Boolean,
    invoiceList:List<InvoiceV2>?
) {

    val scope = rememberCoroutineScope()

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

    var expandableSection by remember { mutableStateOf(AddInvoiceSection.NONE) }


    val context = LocalContext.current
    viewModel.paymentMethod = SharedPreferences.readPaymentMethod(context) ?: ""

    LaunchedEffect(key1 = true) {
        ignoredOnComposing(
            AppBarState(
                title = "ADD INVOICE",
                action = {
                    Row {
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

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
        )
        {

            // Invoice number and date
            ExpandableWithCompletedIconCard(
                title = "# ${InvoiceNumber
                    .getStringNumber(
                        viewModel
                            .getInvoiceV2()
                            .invoiceNumber,
                        time.longValue)}",
                icon = Icons.Default.CalendarMonth,
                isExpanded = expandableSection == AddInvoiceSection.NUMBER,
                onExpandClick = {
                    expandableSection = if (expandableSection == AddInvoiceSection.NUMBER) {
                        AddInvoiceSection.NONE
                    } else {
                        AddInvoiceSection.NUMBER
                    }
                },
                isCompleted = true
            )
            {
                Column(
                    modifier = Modifier
                        .clickable(
                            onClick = {
                                invoiceNumberAlertDialog.value = true
                            }
                        )
                )
                {
//                    BodyLargeText(
//                        text = "Invoice Number: ${InvoiceNumber
//                            .getStringNumber(
//                                viewModel
//                                    .getInvoiceV2()
//                                    .invoiceNumber,
//                                time.longValue)}",
//                        modifier = Modifier
//                            .padding(5.dp)
//                    )
                    Row {
                        BodyLargeText(
                            text = "Date",
                            modifier = Modifier
                                .padding(5.dp)
                        )
                        BodyLargeText(
                            text = DateAndTime.convertLongToDate(time.longValue),
                            modifier = Modifier
                                .padding(5.dp)
                        )
                    }
                    if (modePro) {
                        Row {
                            BodyLargeText(
                                text = "Due date",
                                modifier = Modifier
                                    .padding(5.dp)
                            )
                            if (viewModel.getInvoiceV2().dueDate != null) {
                                BodyLargeText(
                                    text = DateAndTime.convertLongToDate(viewModel.getInvoiceV2().dueDate!!),
                                    modifier = Modifier
                                        .padding(5.dp)
                                )
                            } else {
                                BodyLargeText(
                                    text = "----",
                                    modifier = Modifier
                                        .padding(5.dp)
                                )
                            }

                        }
                    }

                }
            }

            // Client
            ExpandableWithCompletedIconCard(
                title = viewModel.getInvoiceV2().client.clientName.ifEmpty { "Client" },
                icon = Icons.Default.Work,
                isExpanded = expandableSection == AddInvoiceSection.CLIENT,
                onExpandClick = {
                    expandableSection = if (expandableSection == AddInvoiceSection.CLIENT) {
                        AddInvoiceSection.NONE
                    } else {
                        AddInvoiceSection.CLIENT
                    }
                },
                isCompleted = viewModel.getInvoiceV2().client.clientName != ""
            )
            {
                if(viewModel.getInvoiceV2().client.clientName != ""){
                    Column (
                        modifier = Modifier
                            .clickable(
                               onClick = {
                                   navController.navigate(InvoiceCreatorScreen.ChooseClientV2.name)
                               }
                            )
                    ){
                        BodyLargeText(
                            text = viewModel.getInvoiceV2().client.clientAddress1,
                            modifier = Modifier
                                .padding(start = 10.dp, top = 5.dp, bottom = 5.dp)
                        )
                        BodyLargeText(
                            text = viewModel.getInvoiceV2().client.clientAddress2,
                            modifier = Modifier
                                .padding(start = 10.dp, top = 5.dp, bottom = 5.dp)
                        )
                        BodyLargeText(
                            text = viewModel.getInvoiceV2().client.clientCity,
                            modifier = Modifier
                                .padding(start = 10.dp, top = 5.dp, bottom = 10.dp)
                        )
                    }
                }
                else{
                    BodyLargeText(
                        text = "not chosen yet",
                        modifier = Modifier
                            .padding(10.dp)
                            .clickable {
                                navController.navigate(InvoiceCreatorScreen.ChooseClientV2.name)
                            }
                    )
                }

            }


            itemInvoiceList.forEach { itemInvoice ->
                ItemCardViewV2(
                    itemInvoice
                )
            }

            if (!modePro) {
                if (itemInvoiceList.isEmpty()) {
                    EmptyItemCardViewV2(
                        position = 0,
                        showPosition = false,
                        onClick = {
                            navController.navigate(InvoiceCreatorScreen.ChooseItemV2.name)
                        }
                    )
                }
            } else {
                if (itemInvoiceList.size < 10) {
                    EmptyItemCardViewV2(
                        position = if (itemInvoiceList.isEmpty()) 1 else itemInvoiceList.size + 1,
                        showPosition = true,
                        onClick = {
                            navController.navigate(InvoiceCreatorScreen.ChooseItemV2.name)
                        }
                    )
                }
            }

            // Payment method
            ExpandableWithCompletedIconCard(
                title = "Payment Method",
                icon = Icons.Default.CreditCard,
                isExpanded = expandableSection == AddInvoiceSection.PAYMENT_METHOD,
                onExpandClick = {
                    expandableSection = if (expandableSection == AddInvoiceSection.PAYMENT_METHOD) {
                        AddInvoiceSection.NONE
                    } else{
                        AddInvoiceSection.PAYMENT_METHOD
                    }
                },
                isCompleted = viewModel.paymentMethod != "no payment method specified yet"
            )
            {
                BodyLargeText(
                    text = viewModel.paymentMethod,
                    modifier = Modifier
                        .padding(5.dp)
                        .clickable(
                            onClick = {
                                invoicePaymentMethodAlertDialog.value = true
                            }
                        )
                )
            }

            // Signature
            ExpandableWithCompletedIconCard(
                title = "Signature",
                icon = ImageVector.vectorResource(id = R.drawable.signature_24dp_1f1f1f_fill0_wght400_grad0_opsz24),
                isExpanded = expandableSection == AddInvoiceSection.SIGNATURE,
                onExpandClick = {
                    expandableSection = if(expandableSection == AddInvoiceSection.SIGNATURE){
                        AddInvoiceSection.NONE
                    } else{
                        AddInvoiceSection.SIGNATURE
                    }
                },
                isCompleted = file!!.exists()
            )
            {
                if(file.exists()){
                    val options = BitmapFactory.Options()
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888
                    image.value = BitmapFactory.decodeFile(file.path, options)
                    val imageBitmap = image.value
                    if(imageBitmap != null){
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                modifier = Modifier
                                    .clickable(
                                        onClick = {
                                            invoiceSignatureAlertDialog.value = true
                                        }
                                    ),
                                bitmap = imageBitmap.asImageBitmap(),
                                contentDescription = ""
                            )
                        }
                    }

                } else {
                    BodyLargeText(
                        text = "Create signature",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                            onClick = {
                                invoiceSignatureAlertDialog.value = true
                            }
                        ),
                        textAlign = TextAlign.Center
                    )
                }

            }

            // Save button
            CustomButton(
                enabled = viewModel.getInvoiceV2().client.clientName != "" && viewModel.getInvoiceItemList()
                    .isNotEmpty(),
                onClick = {
                    viewModel.saveInvoiceV2()
                    scope.launch(Dispatchers.IO) {
                        PdfUtilsV2.generateInvoicePdfV2(
                            context = context,
                            invoiceV2 = viewModel.getInvoiceV2(),
                            items = viewModel.getInvoiceItemList()
                        )
                        withContext(Dispatchers.Main) {
                            navController.navigateUp()
                        }
                    }

                },
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                text = "SAVE AND PRINT"
            )
        }
    }


    if(invoiceNumberAlertDialog.value){
        AlertDialogInvoiceNumberV2(
            onDismissRequest = {
                invoiceNumberAlertDialog.value = false
            },
            viewModel = viewModel,
            modePro = modePro,
            listOfThisMonthAndYearInvoices = invoiceList?.filter { invoiceV2 ->
                val invoiceMAY = DateAndTime.monthAndYear(invoiceV2.time)
                val currentMAY = DateAndTime.monthAndYear(time.longValue)
                invoiceMAY.year == currentMAY.year && invoiceMAY.month == currentMAY.month
            }
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