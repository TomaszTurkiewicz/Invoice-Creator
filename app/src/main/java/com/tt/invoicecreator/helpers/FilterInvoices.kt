package com.tt.invoicecreator.helpers

import com.tt.invoicecreator.data.roomV2.entities.InvoiceItemV2
import com.tt.invoicecreator.data.roomV2.entities.InvoiceV2
import com.tt.invoicecreator.data.roomV2.entities.PaidV2

object FilterInvoices {
    fun getOverdue(
        invoiceList:List<InvoiceV2>,
        itemList:List<InvoiceItemV2>,
        paidList:List<PaidV2>?
    ):List<InvoiceV2>{
        val overdue = invoiceList.filter {
            val paid = paidList?.filter { paid ->
                paid.invoiceId == it.invoiceId
            }
            val amountPaid = InvoiceValueCalculator.calculatePaid(paid)
            val items = itemList.filter { item ->
                item.invoiceId == it.invoiceId
            }

            it.dueDate != null&&it.dueDate!!< System.currentTimeMillis() && amountPaid < InvoiceValueCalculator.calculateV2(items)
        }
        return overdue

        }
    fun getNotPaid(
        invoiceList:List<InvoiceV2>,
        itemList:List<InvoiceItemV2>,
        paidList:List<PaidV2>?
    ):List<InvoiceV2> {
        val notPaid = invoiceList.filter {
            val paid = paidList?.filter { paid ->
                paid.invoiceId == it.invoiceId
            }
            val amountPaid = InvoiceValueCalculator.calculatePaid(paid)
            val items = itemList.filter { item ->
                item.invoiceId == it.invoiceId
            }
            amountPaid < InvoiceValueCalculator.calculateV2(items)
        }
        return notPaid
    }

    fun getPaid(
        invoiceList:List<InvoiceV2>,
        itemList:List<InvoiceItemV2>,
        paidList:List<PaidV2>?
    ):List<InvoiceV2>{
        val paid = invoiceList.filter {
            val paid = paidList?.filter { paid ->
                paid.invoiceId == it.invoiceId
            }
            val amountPaid = InvoiceValueCalculator.calculatePaid(paid)
            val items = itemList.filter { item ->
                item.invoiceId == it.invoiceId
            }
            amountPaid >= InvoiceValueCalculator.calculateV2(items)
        }
        return paid

    }

    }


