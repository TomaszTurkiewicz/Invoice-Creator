package com.tt.invoicecreator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tt.invoicecreator.data.room.ItemDao

@Suppress("UNCHECKED_CAST")
class AppViewModelFactory(
    private val itemDao: ItemDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AppViewModel::class.java)){
            return AppViewModel(
                itemDao = itemDao
            ) as T
        }
        throw IllegalArgumentException("Unknow ViewModel class")
    }
}