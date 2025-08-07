package com.tt.invoicecreator.data.roomV2

import kotlinx.coroutines.flow.Flow

interface InvoiceItemRepositoryV2 {
    fun getAllInvoiceItems() : Flow<List<InvoiceItemV2>>

    suspend fun insertInvoiceItem(invoiceItemV2: InvoiceItemV2)

    suspend fun deleteInvoiceItem(invoiceItemV2: InvoiceItemV2)
}