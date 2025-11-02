package com.tt.invoicecreator.helpers

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

object DateAndTime {
    fun convertLongToDate(time: Long):String{
        val date = Date(time)
        val format = SimpleDateFormat("yyyy-MM-dd",java.util.Locale.getDefault())
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

    fun getDifferenceInDays(timeStart:Long, timeEnd:Long):Int{
        val dif = timeEnd-timeStart

        return (dif/(1000*60*60*24)).toInt()
    }
}