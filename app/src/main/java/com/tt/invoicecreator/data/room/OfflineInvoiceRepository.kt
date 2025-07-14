package com.tt.invoicecreator.data.room

import kotlinx.coroutines.flow.Flow

class OfflineInvoiceRepository (private val invoiceDao: InvoiceDao) : InvoiceRepository {
    override fun getAllInvoices(): Flow<List<Invoice>> = invoiceDao.getInvoices()

    override suspend fun insertInvoice(invoice: Invoice) = invoiceDao.insert(invoice)
}