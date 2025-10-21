package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.data.roomV2.entities.InvoiceItemV2
import com.tt.invoicecreator.data.roomV2.entities.InvoiceV2
import com.tt.invoicecreator.data.roomV2.entities.PaidV2
import com.tt.invoicecreator.helpers.DateAndTime
import com.tt.invoicecreator.helpers.InvoiceNumber
import com.tt.invoicecreator.helpers.InvoiceValueCalculator
import com.tt.invoicecreator.ui.theme.Typography
import com.tt.invoicecreator.ui.theme.myColors

@Composable
fun SingleRowInvoiceV2(
    invoice: InvoiceV2,
    invoiceItems:List<InvoiceItemV2>,
    paidInvoices: List<PaidV2>?,
    invoiceChosen: (InvoiceV2) -> Unit,
    itemsChosen: (List<InvoiceItemV2>) -> Unit,
    paidChosen: (List<PaidV2>?) -> Unit,
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
                itemsChosen(invoiceItems)
                paidChosen(paidInvoices)
            }
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
        )
        {
            Text(
                text = "Invoice number: ${InvoiceNumber.getStringNumber(invoice.invoiceNumber,invoice.time)}",
                style = Typography.titleLarge,
                color = MaterialTheme.myColors.primaryDark,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Text(
                text = "date: ${DateAndTime.convertLongToDate(invoice.time)}",
                style = Typography.bodyLarge,
                color = MaterialTheme.myColors.primaryDark
            )
            if(modePro && invoice.dueDate != null){
                Text(
                    text = "due date: ${DateAndTime.convertLongToDate(invoice.dueDate!!)}",
                    style = Typography.bodyLarge,
                    color = MaterialTheme.myColors.primaryDark
                )
            }
            Text(
                text = "client: ${invoice.client.clientName}",
                style = Typography.bodyLarge,
                color = MaterialTheme.myColors.primaryDark
            )

            Column {
                Row {
                    Text(
                        text = "value: ${InvoiceValueCalculator.calculateV2(invoiceItems)}",
                        style = Typography.bodyLarge,
                        color = MaterialTheme.myColors.primaryDark,
                        modifier = Modifier
                            .padding(end = 10.dp)
                    )

                    if(modePro){

                        if(amountPaid.doubleValue == InvoiceValueCalculator.calculateV2(invoiceItems)){
                            Text(
                                text = "PAID",
                                style = Typography.bodyLarge,
                                color = MaterialTheme.myColors.success
                            )
                        }else{
                            Text(
                                text = "paid: ${amountPaid.doubleValue}",
                                style = Typography.bodyLarge,
                                color = MaterialTheme.myColors.primaryDark
                            )
                        }


                    }
                }

                if(modePro
                    && invoice.dueDate != null
                    && invoice.dueDate!! < System.currentTimeMillis()
                    && amountPaid.doubleValue < InvoiceValueCalculator.calculateV2(invoiceItems)){
                    Text(
                        text = "OVERDUE",
                        style = Typography.bodyLarge,
                        color = MaterialTheme.myColors.error
                    )
                }

//                if(modePro
//                    && amountPaid.doubleValue == InvoiceValueCalculator.calculateV2(invoiceItems)){
//
//                }
            }




        }
    }
}