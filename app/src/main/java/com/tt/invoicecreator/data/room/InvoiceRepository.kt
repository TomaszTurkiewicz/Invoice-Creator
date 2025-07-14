package com.tt.invoicecreator.data.room

import kotlinx.coroutines.flow.Flow

interface InvoiceRepository {
    fun getAllInvoices(): Flow<List<Invoice>>

    suspend fun insertInvoice(invoice: Invoice)
}