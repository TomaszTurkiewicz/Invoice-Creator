package com.tt.invoicecreator.helpers

import com.tt.invoicecreator.data.room.Invoice

object InvoiceValueCalculator {
    fun calculate(invoice: Invoice):Double{
        return invoice.item.itemValue * invoice.itemCount - invoice.itemDiscount
    }
}