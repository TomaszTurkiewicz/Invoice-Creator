package com.tt.invoicecreator.helpers

import android.icu.util.GregorianCalendar
import java.util.Calendar

object InvoiceDueDate {

    fun getInitialDueDate(
        time:Long
    ): Long{
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        val date = calendar.time

        val newCalendar = GregorianCalendar()
        newCalendar.time = date
        newCalendar.add(Calendar.DATE,14)

        return newCalendar.time.time
    }


}