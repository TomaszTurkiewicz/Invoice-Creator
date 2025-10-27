package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.helpers.DateAndTime
import com.tt.invoicecreator.helpers.InvoiceNumber
import com.tt.invoicecreator.ui.components.texts.BodyLargeText
import com.tt.invoicecreator.ui.theme.Typography
import com.tt.invoicecreator.ui.theme.myColors


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