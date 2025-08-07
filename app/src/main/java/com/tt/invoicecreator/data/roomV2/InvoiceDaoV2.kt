package com.tt.invoicecreator.data.roomV2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface InvoiceDaoV2 {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(invoiceV2: InvoiceV2)

    @Query("Select * from invoicev2")
    fun getInvoices(): Flow<List<InvoiceV2>>
}