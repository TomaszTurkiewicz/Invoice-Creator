package com.tt.invoicecreator.helpers

import com.tt.invoicecreator.data.room.Invoice
import com.tt.invoicecreator.data.roomV2.InvoiceItemV2
import com.tt.invoicecreator.data.roomV2.InvoiceV2

object InvoiceValueCalculator {
    fun calculate(invoice: Invoice):Double{
        return invoice.item.itemValue * invoice.itemCount - invoice.itemDiscount
    }

    fun calculateV2(items:List<InvoiceItemV2>):Double{
        val value: ArrayList<Double> = ArrayList()

        items.forEach{
            value.add(it.itemV2.itemValue * it.itemCount - it.itemDiscount)
        }

        var sum = 0.0

        value.forEach{
            sum += it
        }
        return sum
    }

    fun calculateV2oneItem(itemV2: InvoiceItemV2): Double {
        return itemV2.itemV2.itemValue * itemV2.itemCount - itemV2.itemDiscount
    }
}