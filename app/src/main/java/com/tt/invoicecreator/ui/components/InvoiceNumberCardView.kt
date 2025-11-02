package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.helpers.DateAndTime
import com.tt.invoicecreator.helpers.InvoiceNumber
import com.tt.invoicecreator.ui.components.texts.BodyLargeText


@Composable
fun InvoiceNumberCardView(
    number:Int,
    time:Long,
    modePro:Boolean,
    dueDate:Long?,
    onClick: () -> Unit
) {

    val invoiceNumberString = InvoiceNumber.getStringNumber(number,time)


    CustomCardView(
        modifier = Modifier
            .clickable {
                onClick()
            }
    ) {
        Column()
        {
            BodyLargeText(
                text = "Invoice Number: $invoiceNumberString",
                modifier = Modifier
                    .padding(5.dp))
            Row {
                BodyLargeText(
                    text = "Date",
                    modifier = Modifier
                        .padding(5.dp))
                BodyLargeText(
                    text = DateAndTime.convertLongToDate(time),
                    modifier = Modifier
                        .padding(5.dp))
            }
            if(modePro){
                Row{
                    BodyLargeText(
                        text = "Due date",
                        modifier = Modifier
                            .padding(5.dp))
                    if(dueDate!=null){
                        BodyLargeText(
                            text = DateAndTime.convertLongToDate(dueDate),
                            modifier = Modifier
                                .padding(5.dp))
                    }
                    else{
                        BodyLargeText(
                            text = "----",
                            modifier = Modifier
                                .padding(5.dp))
                    }

                }
            }

        }
    }
}