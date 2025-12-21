package com.tt.invoicecreator.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.data.AppUiState
import com.tt.invoicecreator.data.roomV2.entities.InvoiceItemV2
import com.tt.invoicecreator.data.roomV2.entities.InvoiceV2
import com.tt.invoicecreator.data.roomV2.entities.PaidV2
import com.tt.invoicecreator.helpers.Currency
import com.tt.invoicecreator.helpers.CurrencyFormatter
import com.tt.invoicecreator.helpers.DateAndTime
import com.tt.invoicecreator.helpers.InvoiceNumber
import com.tt.invoicecreator.helpers.InvoiceValueCalculator
import com.tt.invoicecreator.helpers.PdfUtilsV2
import com.tt.invoicecreator.ui.alert_dialogs.AlertDialogPayInvoiceV2
import com.tt.invoicecreator.ui.components.CustomButton
import com.tt.invoicecreator.ui.components.FinancialRowItem
import com.tt.invoicecreator.ui.components.InfoRowWithIcon
import com.tt.invoicecreator.ui.components.PaymentHistoryRow
import com.tt.invoicecreator.ui.components.badge.StatusBadge
import com.tt.invoicecreator.ui.components.cards.CustomCardView
import com.tt.invoicecreator.ui.components.texts.BodyLargeText
import com.tt.invoicecreator.ui.components.texts.TitleLargeText
import com.tt.invoicecreator.ui.theme.myColors
import com.tt.invoicecreator.viewmodel.AppViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun InvoiceInfoScreenV2(
    invoiceV2: InvoiceV2,
    ignoredOnComposing:(AppBarState) -> Unit,
    viewModel: AppViewModel,
    uiState: AppUiState,
    paidInvoicesCollection: List<PaidV2>?,
    invoiceItemsCollection: List<InvoiceItemV2>
    ) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Loading State for PDF generation
    var isGeneratingPdf by remember { mutableStateOf(false) }

    val payAlertDialog = remember {
        mutableStateOf(false)
    }

    // --- LOGIC / DATA PREP ---
    val invoiceItemListV2 = remember(invoiceItemsCollection, invoiceV2) {
        invoiceItemsCollection.filter { it.invoiceId == invoiceV2.invoiceId }
    }

    // Safety check: if no items, default to USD
    val currency = invoiceItemListV2.firstOrNull()?.itemV2?.itemCurrency ?: Currency.GBP
    val formatter = remember { CurrencyFormatter() }

    val invoiceNettoValue = remember(invoiceItemListV2) {
        InvoiceValueCalculator.calculateNettoV2(invoiceItemListV2)
    }
    val invoiceVATValue = remember(invoiceItemListV2) {
        InvoiceValueCalculator.calculateVATV2(invoiceItemListV2)
    }
    val invoiceGrossValue = remember(invoiceItemListV2) {
        InvoiceValueCalculator.calculateV2(invoiceItemListV2)
    }

    val invoiceValue = remember {
        mutableDoubleStateOf(InvoiceValueCalculator.calculateV2(invoiceItemListV2))
    }


    val paidListV2 = remember(paidInvoicesCollection, invoiceV2) {
        paidInvoicesCollection?.filter { it.invoiceId == invoiceV2.invoiceId } ?: emptyList()
    }

    val amountPaid = remember(paidListV2) { InvoiceValueCalculator.calculatePaid(paidListV2) }

    val isPaidInFull = amountPaid >= invoiceGrossValue




    // --- TOP BAR SETUP ---
    LaunchedEffect(key1 = true) {
        ignoredOnComposing(
            AppBarState(
                title = "INFO",
                action = null
            )
        )
    }

    // --- UI CONTENT ---
    CustomCardView {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(bottom = 20.dp)
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TitleLargeText(
                    text = "Invoice #${
                        InvoiceNumber.getStringNumber(
                            invoiceV2.invoiceNumber,
                            invoiceV2.time
                        )
                    }",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.padding(16.dp))

                // STATUS BADGE
                StatusBadge(
                    isPaidInFull = isPaidInFull,
                    remaining = invoiceGrossValue - amountPaid,
                    currency = currency,
                    formatter = formatter
                )
            }

            Spacer(modifier = Modifier.padding(8.dp))
            Divider(color = Color.LightGray.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.padding(8.dp))

            // DATES SECTION
            InfoRowWithIcon(
                icon = Icons.Default.DateRange,
                label = "Date Issued",
                value = DateAndTime.convertLongToDate(invoiceV2.time)
            )

            if(invoiceV2.dueDate != null){
                Spacer(modifier = Modifier.height(8.dp))
                InfoRowWithIcon(
                    icon = Icons.Default.DateRange,
                    label = "Due Date",
                    value = DateAndTime.convertLongToDate(invoiceV2.dueDate!!)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // CLIENT SECTION
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .padding(top = 30.dp)
            ){
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.myColors.invoicePrimary,
                    modifier = Modifier
                        .padding(start = 2.dp, end = 12.dp)
                        .size(24.dp)
                )
                Column {
                    Text(
                        text = "Bill To:",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    BodyLargeText(text = invoiceV2.client.clientName)
                    BodyLargeText(text = invoiceV2.client.clientAddress1)
                    if(invoiceV2.client.clientAddress2.isNotEmpty()){
                        BodyLargeText(text = invoiceV2.client.clientAddress2)
                    }
                    BodyLargeText(text = invoiceV2.client.clientCity)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // FINANCIAL SUMMARY CARD

            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )
            {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    FinancialRowItem(
                        label = "Net Value",
                        value = invoiceNettoValue,
                        currency = currency,
                        formatter = formatter
                    )
                    FinancialRowItem(
                        label = "VAT",
                        value = invoiceVATValue,
                        currency = currency,
                        formatter = formatter
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color =  Color.Gray.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(8.dp))

                    FinancialRowItem(
                        label = "TOTAL GROSS",
                        value = invoiceGrossValue,
                        currency = currency,
                        formatter = formatter,
                        isTotal = true
                    )

                    if(!isPaidInFull && amountPaid > 0){
                        Spacer(modifier = Modifier.height(8.dp))
                        FinancialRowItem(
                            label = "Already Paid",
                            value = amountPaid,
                            currency = currency,
                            formatter = formatter,
                            color = MaterialTheme.myColors.success
                        )
                    }
                }
            }

            // HISTORY SECTION

            if(paidListV2.isNotEmpty()){
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Payment History",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                paidListV2.forEach { paidV2 ->
                    PaymentHistoryRow(
                        paidV2 = paidV2,
                        currency = currency
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // ACTION BUTTONS

            if (!isPaidInFull) {
                CustomButton(
                    onClick = { payAlertDialog.value = true },
                    modifier = Modifier.fillMaxWidth(),
                    text = "REGISTER PAYMENT"
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            CustomButton(
                onClick = {
                    isGeneratingPdf = true
                    scope.launch(Dispatchers.IO) {
                        try {
                            PdfUtilsV2.generateInfoPdfV2(
                                context = context,
                                invoiceV2 = invoiceV2,
                                items = invoiceItemListV2,
                                paidList = paidListV2
                            )
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Saved to Downloads", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    context,
                                    "Error: ${e.localizedMessage}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } finally {
                            withContext(Dispatchers.Main) {
                                isGeneratingPdf = false
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = !isGeneratingPdf, // Disable double clicks
                text = if(isGeneratingPdf) "Generating PDF..." else "PRINT INVOICE"
                )
        }
    }



    if(payAlertDialog.value){
        AlertDialogPayInvoiceV2(
            viewModel = viewModel,
            invoiceId = invoiceV2.invoiceId,
            paidListV2 = paidListV2,
            uiState = uiState,
            invoiceValue = invoiceGrossValue,
            paidInvoicesCollection = paidInvoicesCollection,
            currency = currency,
            closeAlertDialog = {
                payAlertDialog.value = false
            }
        )
    }

}