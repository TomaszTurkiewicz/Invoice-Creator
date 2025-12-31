package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableLongState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tt.invoicecreator.helpers.DateAndTime
import com.tt.invoicecreator.ui.components.texts.BodyLargeText
import com.tt.invoicecreator.viewmodel.AppViewModel

@Composable
fun InvoiceNumberAndDateComponent(
    modePro: Boolean,
    viewModel: AppViewModel,
    time: MutableLongState,
    onClick: () -> Unit
)
{
    Column(
        modifier = Modifier
            .clickable(
                onClick = {
                    onClick()
                }
            )
    )
    {

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