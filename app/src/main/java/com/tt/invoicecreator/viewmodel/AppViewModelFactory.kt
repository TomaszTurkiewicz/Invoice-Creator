package com.tt.invoicecreator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tt.invoicecreator.data.roomV2.dao.ClientDaoV2
import com.tt.invoicecreator.data.roomV2.dao.InvoiceDaoV2
import com.tt.invoicecreator.data.roomV2.dao.InvoiceItemDaoV2
import com.tt.invoicecreator.data.roomV2.dao.ItemDaoV2
import com.tt.invoicecreator.data.roomV2.dao.PaidDaoV2

@Suppress("UNCHECKED_CAST")
class AppViewModelFactory(
    private val itemDaoV2: ItemDaoV2,
    private val clientDaoV2: ClientDaoV2,
    private val invoiceItemDaoV2: InvoiceItemDaoV2,
    private val invoiceDaoV2: InvoiceDaoV2,
    private val paidDaoV2: PaidDaoV2
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AppViewModel::class.java)){
            return AppViewModel(
                itemDaoV2 = itemDaoV2,
                clientDaoV2 = clientDaoV2,
                invoiceItemDaoV2 = invoiceItemDaoV2,
                invoiceDaoV2 = invoiceDaoV2,
                paidDaoV2 = paidDaoV2
            ) as T
        }
        throw IllegalArgumentException("Unknow ViewModel class")
    }
}