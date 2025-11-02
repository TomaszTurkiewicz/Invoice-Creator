package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.roomV2.entities.InvoiceItemV2
import com.tt.invoicecreator.data.roomV2.entities.InvoiceV2
import com.tt.invoicecreator.data.roomV2.entities.PaidV2
import com.tt.invoicecreator.helpers.DateAndTime
import com.tt.invoicecreator.helpers.InvoiceNumber
import com.tt.invoicecreator.helpers.InvoiceValueCalculator
import com.tt.invoicecreator.ui.components.texts.BodyLargeText
import com.tt.invoicecreator.ui.theme.myColors

@Composable
fun SingleRowInvoiceV2(
    invoice: InvoiceV2,
    invoiceItems:List<InvoiceItemV2>,
    paidInvoices: List<PaidV2>?,
    invoiceChosen: (InvoiceV2) -> Unit,
    modePro:Boolean
) {

    val amountPaid = remember {
        mutableDoubleStateOf(0.0)
    }

    amountPaid.doubleValue = InvoiceValueCalculator.calculatePaid(paidInvoices)

    CustomCardView(
        modifier = Modifier
            .clickable {
                invoiceChosen(invoice)
            }
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
        )
        {
            BodyLargeText(
                text = "Invoice number: ${InvoiceNumber.getStringNumber(invoice.invoiceNumber,invoice.time)}",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
            BodyLargeText(
                text = "date: ${DateAndTime.convertLongToDate(invoice.time)}"
            )
            if(modePro && invoice.dueDate != null){
                BodyLargeText(
                    text = "due date: ${DateAndTime.convertLongToDate(invoice.dueDate!!)}"
                )
            }
            BodyLargeText(
                text = "client: ${invoice.client.clientName}"
            )

            Column {
                Row {
                    BodyLargeText(
                        text = "value: ${InvoiceValueCalculator.calculateV2(invoiceItems)}",
                        modifier = Modifier
                            .padding(end = 10.dp)
                    )

                    if(modePro){

                        if(amountPaid.doubleValue == InvoiceValueCalculator.calculateV2(invoiceItems)){
                            BodyLargeText(
                                text = "PAID",
                                color = MaterialTheme.myColors.success
                            )
                        }else{
                            BodyLargeText(
                                text = "paid: ${amountPaid.doubleValue}",
                                color = MaterialTheme.myColors.primaryDark
                            )
                        }
                    }
                }
                if(modePro
                    && invoice.dueDate != null
                    && invoice.dueDate!! < System.currentTimeMillis()
                    && amountPaid.doubleValue < InvoiceValueCalculator.calculateV2(invoiceItems)){
                    BodyLargeText(
                        text = "OVERDUE",
                        color = MaterialTheme.myColors.error
                    )
                }
            }
        }
    }
}