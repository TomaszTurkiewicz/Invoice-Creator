package com.tt.invoicecreator.helpers

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

object DateAndTime {
    fun convertLongToDate(time: Long):String{
        val date = Date(time)
        val format = SimpleDateFormat("yyyy-MM-dd")
        return format.format(date)
    }

    fun monthAndYear(time:Long):MonthAndYear{
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        return MonthAndYear(
            month = calendar.get(Calendar.MONTH)+1,
            year = calendar.get(Calendar.YEAR)
        )
    }
}