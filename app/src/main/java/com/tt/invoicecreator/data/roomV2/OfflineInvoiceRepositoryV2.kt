package com.tt.invoicecreator.data.roomV2

import kotlinx.coroutines.flow.Flow

class OfflineInvoiceRepositoryV2 (private val invoiceDaoV2: InvoiceDaoV2) : InvoiceRepositoryV2 {
    override fun getAllInvoices(): Flow<List<InvoiceV2>> = invoiceDaoV2.getInvoices()

    override suspend fun insertInvoice(invoiceV2: InvoiceV2):Long = invoiceDaoV2.insert(invoiceV2)
}