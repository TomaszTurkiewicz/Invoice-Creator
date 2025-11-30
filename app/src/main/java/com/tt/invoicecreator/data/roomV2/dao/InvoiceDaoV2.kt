package com.tt.invoicecreator.data.roomV2.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tt.invoicecreator.data.roomV2.entities.InvoiceV2
import kotlinx.coroutines.flow.Flow

@Dao
interface InvoiceDaoV2 {

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insert(invoiceV2: InvoiceV2):Long

    @Query("Select * from invoicev2")
    fun getInvoices(): Flow<List<InvoiceV2>>

    @Query("Select * from invoicev2")
    suspend fun getInvoicesDirectly():List<InvoiceV2>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertWithId(invoiceV2: InvoiceV2)

    @Query("DELETE FROM invoiceV2")
    suspend fun deleteAll()

}