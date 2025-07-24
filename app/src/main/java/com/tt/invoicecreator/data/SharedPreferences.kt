package com.tt.invoicecreator.data

import android.content.Context
import androidx.core.content.edit
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
}