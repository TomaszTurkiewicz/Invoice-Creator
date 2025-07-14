package com.tt.invoicecreator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tt.invoicecreator.data.room.Client
import com.tt.invoicecreator.data.room.ClientDao
import com.tt.invoicecreator.data.room.InvoiceDao
import com.tt.invoicecreator.data.room.ItemDao

@Suppress("UNCHECKED_CAST")
class AppViewModelFactory(
    private val itemDao: ItemDao,
    private val clientDao: ClientDao,
    private val invoiceDao: InvoiceDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AppViewModel::class.java)){
            return AppViewModel(
                itemDao = itemDao,
                clientDao = clientDao,
                invoiceDao = invoiceDao
            ) as T
        }
        throw IllegalArgumentException("Unknow ViewModel class")
    }
}