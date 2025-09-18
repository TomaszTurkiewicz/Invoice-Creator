package com.tt.invoicecreator.helpers

import com.tt.invoicecreator.data.roomV2.entities.InvoiceV2


object InvoiceNumber {


    fun getNewNumberV2(
        time:Long,
        invoices:List<InvoiceV2>?
    ):Int{
        val currentMonthAndYear = DateAndTime.monthAndYear(time)
        var invoiceNumber = 1

        val invoicesNew = invoices?.filter { it ->
            val monthAndYear = DateAndTime.monthAndYear(it.time)
            monthAndYear.month == currentMonthAndYear.month && monthAndYear.year == currentMonthAndYear.year
        }

        return  if(invoicesNew.isNullOrEmpty()){
            invoiceNumber
        }else{
            invoicesNew.forEach{
                invoice ->

                    if(invoice.invoiceNumber>invoiceNumber){
                        invoiceNumber = invoice.invoiceNumber
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

    fun getStringMonthAndYear(
        time:Long
    ):String{
        val monthAndYear = DateAndTime.monthAndYear(time)
        val monthTen = monthAndYear.month/10
        val monthOne = monthAndYear.month%10
        val year = monthAndYear.year%100
        return "$monthTen$monthOne$year"
    }
}