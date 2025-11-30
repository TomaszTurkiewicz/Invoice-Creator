package com.tt.invoicecreator.data.roomV2.repository

import com.tt.invoicecreator.data.roomV2.entities.InvoiceItemV2
import kotlinx.coroutines.flow.Flow

interface InvoiceItemRepositoryV2 {
    fun getAllInvoiceItems() : Flow<List<InvoiceItemV2>>

    suspend fun insertInvoiceItem(invoiceItemV2: InvoiceItemV2)

    suspend fun getAllInvoiceItemsDirectly() : List<InvoiceItemV2>

    suspend fun insertWithId(invoiceItemV2: InvoiceItemV2)

    suspend fun deleteAllInvoiceItems()

}