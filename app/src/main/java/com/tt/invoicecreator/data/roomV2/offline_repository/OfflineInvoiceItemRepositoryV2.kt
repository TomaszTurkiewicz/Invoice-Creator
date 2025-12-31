package com.tt.invoicecreator.data.roomV2.offline_repository

import com.tt.invoicecreator.data.roomV2.dao.InvoiceItemDaoV2
import com.tt.invoicecreator.data.roomV2.entities.InvoiceItemV2
import com.tt.invoicecreator.data.roomV2.repository.InvoiceItemRepositoryV2
import kotlinx.coroutines.flow.Flow

class OfflineInvoiceItemRepositoryV2 (private val invoiceItemDaoV2: InvoiceItemDaoV2) :
    InvoiceItemRepositoryV2 {
    override fun getAllInvoiceItems(): Flow<List<InvoiceItemV2>> = invoiceItemDaoV2.getInvoiceItems()

    override suspend fun insertInvoiceItem(invoiceItemV2: InvoiceItemV2) = invoiceItemDaoV2.insert(invoiceItemV2)
    override suspend fun getAllInvoiceItemsDirectly(): List<InvoiceItemV2> = invoiceItemDaoV2.getInvoiceItemsDirectly()

    override suspend fun insertWithId(invoiceItemV2: InvoiceItemV2) = invoiceItemDaoV2.insertWithId(invoiceItemV2)

    override suspend fun deleteAllInvoiceItems() = invoiceItemDaoV2.deleteAll()

}