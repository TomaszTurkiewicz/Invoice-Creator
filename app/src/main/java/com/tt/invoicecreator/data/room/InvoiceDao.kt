package com.tt.invoicecreator.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface InvoiceDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(invoice: Invoice)

    @Query("Select * from invoice")
    fun getInvoices(): Flow<List<Invoice>>
}