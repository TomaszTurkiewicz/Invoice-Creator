package com.tt.invoicecreator.data.roomV2

import kotlinx.coroutines.flow.Flow

class OfflineInvoiceItemRepositoryV2 (private val invoiceItemDaoV2: InvoiceItemDaoV2) : InvoiceItemRepositoryV2 {
    override fun getAllInvoiceItems(): Flow<List<InvoiceItemV2>> = invoiceItemDaoV2.getInvoiceItems()

    override suspend fun insertInvoiceItem(invoiceItemV2: InvoiceItemV2) = invoiceItemDaoV2.insert(invoiceItemV2)

    override suspend fun deleteInvoiceItem(invoiceItemV2: InvoiceItemV2) = invoiceItemDaoV2.deleteInvoiceItem(invoiceItemV2)
}