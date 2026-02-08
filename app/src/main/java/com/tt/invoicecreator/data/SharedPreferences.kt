package com.tt.invoicecreator.data

import android.content.Context
import androidx.core.content.edit
import com.tt.invoicecreator.helpers.Currency
import com.tt.invoicecreator.helpers.User

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

    fun saveUserDetails(context: Context, user: User){
        val sp = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
        sp.edit{
            putString("userName",user.userName)
            putString("userAddressLine1",user.userAddressLine1)
            putString("userAddressLine2",user.userAddressLine2)
            putString("userCity",user.userCity)
        }
    }

    fun readUserDetails(context: Context):User{
        val sp = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
        return User(
            userName = sp.getString("userName",""),
            userAddressLine1 = sp.getString("userAddressLine1", ""),
            userAddressLine2 = sp.getString("userAddressLine2", ""),
            userCity = sp.getString("userCity", "")
        )
    }

//    fun savePROMode(context: Context, PROMode:Boolean){
//        val sp = context.getSharedPreferences("PRO_MODE", Context.MODE_PRIVATE)
//        sp.edit {
//            putBoolean("pro_mode", PROMode)
//        }
//    }
//
//    fun readPROMode(context: Context):Boolean{
//        val sp = context.getSharedPreferences("PRO_MODE", Context.MODE_PRIVATE)
//        return sp.getBoolean("pro_mode", false)
//    }

    fun saveCurrency(context: Context, currency:String){
        val sp = context.getSharedPreferences("CURRENCY", Context.MODE_PRIVATE)
        sp.edit {
            putString("currency", currency)
        }
    }

    fun readCurrency(context: Context): Currency {
        val sp = context.getSharedPreferences("CURRENCY", Context.MODE_PRIVATE)
        // Read the saved currency name, default to GBP if nothing is saved
        val currencyName = sp.getString("currency", Currency.GBP.name)
        // Find the matching enum entry, or return GBP if not found
        return Currency.entries.find { it.name == currencyName } ?: Currency.GBP
    }

}