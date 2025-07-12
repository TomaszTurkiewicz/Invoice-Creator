package com.tt.invoicecreator.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(client: Client)

    @Query("Select * from client")
    fun getClients() : Flow<List<Client>>

    @Delete
    suspend fun deleteClient(client: Client)
}