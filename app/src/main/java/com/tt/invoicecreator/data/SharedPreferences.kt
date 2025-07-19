package com.tt.invoicecreator.data

import android.content.Context
import androidx.core.content.edit

object SharedPreferences {
    fun readPaymentMethod(context:Context):String?{
        val sp = context.getSharedPreferences("Payment Method", Context.MODE_PRIVATE)
        return sp.getString("Payment Method", "no payment method specified yet")
    }

    fun savePaymentMethod(context: Context, paymentMethod:String){
        val sp = context.getSharedPreferences("Payment Method", Context.MODE_PRIVATE)
        sp.edit {
            putString("Payment Method", paymentMethod)
        }
    }
}