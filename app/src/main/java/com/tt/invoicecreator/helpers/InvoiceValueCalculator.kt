package com.tt.invoicecreator.helpers

import com.tt.invoicecreator.data.room.Invoice
import com.tt.invoicecreator.data.roomV2.InvoiceV2

object InvoiceValueCalculator {
    fun calculate(invoice: Invoice):Double{
        return invoice.item.itemValue * invoice.itemCount - invoice.itemDiscount
    }
}