package com.tt.invoicecreator.helpers

import android.icu.util.GregorianCalendar
import java.util.Calendar

object InvoiceDueDate {


    fun getDueDate(
        time: Long,
        days: Int = 14
    ): Long{
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        val date = calendar.time

        val newCalendar = GregorianCalendar()
        newCalendar.time = date
        newCalendar.add(Calendar.DATE,days)

        return newCalendar.time.time
    }
}