package com.tt.invoicecreator.helpers

import com.tt.invoicecreator.data.roomV2.InvoiceV2


object InvoiceNumber {


    fun getNewNumberV2(
        time:Long,
        invoices:List<InvoiceV2>?
    ):Int{
        val currentMonthAndYear = DateAndTime.monthAndYear(time)
        var invoiceNumber = 1

        return  if(invoices.isNullOrEmpty()){
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
                    //do nothing
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
        val monthTen = monthAndYear.month/10
        val monthOne = monthAndYear.month%10
        val year = monthAndYear.year%100
        return "$invoiceNumber$monthTen$monthOne$year"
    }
}