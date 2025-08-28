package com.tt.invoicecreator.data.roomV2.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tt.invoicecreator.data.roomV2.entities.InvoiceItemV2
import kotlinx.coroutines.flow.Flow

@Dao
interface InvoiceItemDaoV2 {

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insert(invoiceItemV2: InvoiceItemV2)

    @Query("Select * from invoiceitemv2")
    fun getInvoiceItems() : Flow<List<InvoiceItemV2>>

    @Delete
    suspend fun deleteInvoiceItem(invoiceItemV2: InvoiceItemV2)
}