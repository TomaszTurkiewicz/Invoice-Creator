package com.tt.invoicecreator.data.roomV2.offline_repository

import com.tt.invoicecreator.data.roomV2.dao.InvoiceDaoV2
import com.tt.invoicecreator.data.roomV2.entities.InvoiceV2
import com.tt.invoicecreator.data.roomV2.repository.InvoiceRepositoryV2
import kotlinx.coroutines.flow.Flow

class OfflineInvoiceRepositoryV2 (private val invoiceDaoV2: InvoiceDaoV2) : InvoiceRepositoryV2 {
    override fun getAllInvoices(): Flow<List<InvoiceV2>> = invoiceDaoV2.getInvoices()

    override suspend fun insertInvoice(invoiceV2: InvoiceV2):Long = invoiceDaoV2.insert(invoiceV2)
    override suspend fun getAllInvoicesDirectly(): List<InvoiceV2> {
        return invoiceDaoV2.getInvoicesDirectly()
    }

    override suspend fun insertWithId(invoiceV2: InvoiceV2) = invoiceDaoV2.insertWithId(invoiceV2)

    override suspend fun deleteAllInvoices() = invoiceDaoV2.deleteAll()

}