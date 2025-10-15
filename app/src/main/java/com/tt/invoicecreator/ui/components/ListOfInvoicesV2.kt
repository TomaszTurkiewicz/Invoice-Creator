package com.tt.invoicecreator.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tt.invoicecreator.data.InvoiceStatus
import com.tt.invoicecreator.data.roomV2.entities.InvoiceItemV2
import com.tt.invoicecreator.data.roomV2.entities.InvoiceV2
import com.tt.invoicecreator.data.roomV2.entities.PaidV2
import com.tt.invoicecreator.helpers.InvoiceValueCalculator

@Composable
fun ListOfInvoicesV2(
    invoiceStatus: Enum<InvoiceStatus>,
    list:List<InvoiceV2>,
    itemList:List<InvoiceItemV2>,
    paidInvoices: List<PaidV2>?,
    invoiceChosen: (InvoiceV2) -> Unit,
    itemsChosen: (List<InvoiceItemV2>) -> Unit,
    paidChosen: (List<PaidV2>?) -> Unit,
    modePro:Boolean
) {
    val newList = when(invoiceStatus) {
        InvoiceStatus.ALL -> list
        InvoiceStatus.OVERDUE -> list.filter {
            val paid = paidInvoices?.filter { paid ->
                paid.invoiceId == it.invoiceId
            }
            val amountPaid = InvoiceValueCalculator.calculatePaid(paid)

            val items = itemList.filter { item ->
                item.invoiceId == it.invoiceId
            }


            it.dueDate != null&&it.dueDate!!< System.currentTimeMillis() && amountPaid < InvoiceValueCalculator.calculateV2(items)
        }
        InvoiceStatus.NOT_PAID -> list.filter {
            val paid = paidInvoices?.filter { paid ->
                paid.invoiceId == it.invoiceId
            }
            val amountPaid = InvoiceValueCalculator.calculatePaid(paid)

            val items = itemList.filter { item ->
                item.invoiceId == it.invoiceId
            }
            amountPaid < InvoiceValueCalculator.calculateV2(items)
        }
        InvoiceStatus.PAID -> list.filter {
            val paid = paidInvoices?.filter { paid ->
                paid.invoiceId == it.invoiceId
            }
            val amountPaid = InvoiceValueCalculator.calculatePaid(paid)

            val items = itemList.filter { item ->
                item.invoiceId == it.invoiceId
            }
            amountPaid >= InvoiceValueCalculator.calculateV2(items)
        }
        else -> list
    }


                LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                ) {
            items(
                items = newList.reversed()
            ){
                    invoice ->
                val invoiceItems = itemList.filter {
                    it.invoiceId == invoice.invoiceId
                }

                val paidItems = paidInvoices?.filter {
                    it.invoiceId == invoice.invoiceId
                }

                SingleRowInvoiceV2(
                    invoice = invoice,
                    invoiceItems = invoiceItems,
                    paidInvoices = paidItems,
                    invoiceChosen = {
                        invoiceChosen(it)
                    },
                    itemsChosen = {
                        itemsChosen(it)
                    },
                    paidChosen = {
                        paidChosen(it)
                    },
                    modePro = modePro
                )
            }
        }
    }