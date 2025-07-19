package com.tt.invoicecreator.helpers

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
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

    fun isCurrentMonth(
        monthAndYear1: MonthAndYear,
        monthAndYear2: MonthAndYear
    ):Boolean{
        return (monthAndYear1.year == monthAndYear2.year)&&(monthAndYear1.month == monthAndYear2.month)
    }
}