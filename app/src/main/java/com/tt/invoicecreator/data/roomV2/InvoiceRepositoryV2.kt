package com.tt.invoicecreator.data.roomV2


import kotlinx.coroutines.flow.Flow

interface InvoiceRepositoryV2 {
    fun getAllInvoices(): Flow<List<InvoiceV2>>

    suspend fun insertInvoice(invoiceV2: InvoiceV2)
}