package com.tt.invoicecreator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tt.invoicecreator.data.room.ClientDao
import com.tt.invoicecreator.data.room.InvoiceDao
import com.tt.invoicecreator.data.room.ItemDao
import com.tt.invoicecreator.data.roomV2.ClientDaoV2
import com.tt.invoicecreator.data.roomV2.InvoiceDaoV2
import com.tt.invoicecreator.data.roomV2.InvoiceItemDaoV2
import com.tt.invoicecreator.data.roomV2.ItemDaoV2

@Suppress("UNCHECKED_CAST")
class AppViewModelFactory(
    private val itemDao: ItemDao,
    private val clientDao: ClientDao,
    private val invoiceDao: InvoiceDao,
    private val itemDaoV2: ItemDaoV2,
    private val clientDaoV2: ClientDaoV2,
    private val invoiceItemDaoV2: InvoiceItemDaoV2,
    private val invoiceDaoV2: InvoiceDaoV2
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AppViewModel::class.java)){
            return AppViewModel(
                itemDao = itemDao,
                clientDao = clientDao,
                invoiceDao = invoiceDao,
                itemDaoV2 = itemDaoV2,
                clientDaoV2 = clientDaoV2,
                invoiceItemDaoV2 = invoiceItemDaoV2,
                invoiceDaoV2 = invoiceDaoV2
            ) as T
        }
        throw IllegalArgumentException("Unknow ViewModel class")
    }
}