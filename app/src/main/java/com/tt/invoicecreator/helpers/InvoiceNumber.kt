package com.tt.invoicecreator.helpers

import android.os.Build
import androidx.annotation.RequiresApi
import com.tt.invoicecreator.data.room.Invoice
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId


object InvoiceNumber {
    fun getNewNumber(
        time:Long,
        invoices:List<Invoice>?
    ):Int{
        val currentMonthAndYear = DateAndTime.monthAndYear(time)
        var invoiceNumber = 1

        return if(invoices.isNullOrEmpty()){
             invoiceNumber
        }else{
            invoices.forEach{
                invoice ->
                    val invoiceMonthAndYear = DateAndTime.monthAndYear(invoice.time)
                if(DateAndTime.isCurrentMonth(currentMonthAndYear,invoiceMonthAndYear)){
                    if(invoice.invoiceNumber>invoiceNumber){
                        invoiceNumber = invoice.invoiceNumber
                    }
                }else{
                    // do nothing
                }
            }

            return invoiceNumber+1
        }
    }

    fun getStringNumber(
        invoiceNumber:Int,
        time:Long
    ):String{
        val monthAndYear = DateAndTime.monthAndYear(time)
        return "$invoiceNumber/${monthAndYear.month}/${monthAndYear.year}"
    }
}