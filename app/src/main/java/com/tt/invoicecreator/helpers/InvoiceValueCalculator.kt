package com.tt.invoicecreator.helpers

import com.tt.invoicecreator.data.roomV2.entities.InvoiceItemV2
import com.tt.invoicecreator.data.roomV2.entities.PaidV2

object InvoiceValueCalculator {


    fun calculateV2(items:List<InvoiceItemV2>):Double{
        val value: ArrayList<Double> = ArrayList()

        items.forEach{
            val mVat: Double = if(it.vat == null) 1.0 else (100+it.vat!!)/100
            value.add((it.itemV2.itemValue * it.itemCount - it.itemDiscount) * mVat)
        }

        var sum = 0.0

        value.forEach{
            sum += it
        }
        return sum
    }

    fun calculateNettoV2(items:List<InvoiceItemV2>):Double{
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

    fun calculateVATV2(items:List<InvoiceItemV2>):Double{
        val value: ArrayList<Double> = ArrayList()

        items.forEach{
            val mVat: Double = if(it.vat == null) 0.0 else (it.vat!!)/100
            value.add((it.itemV2.itemValue * it.itemCount - it.itemDiscount) * mVat)
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

    fun calculatePaid(paid:List<PaidV2>?): Double {
        var sum = 0.0

        paid?.forEach{
            sum += it.amountPaid
        }

        return sum

    }
}