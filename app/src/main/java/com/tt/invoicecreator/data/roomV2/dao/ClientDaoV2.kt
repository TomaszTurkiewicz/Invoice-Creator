package com.tt.invoicecreator.data.roomV2.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tt.invoicecreator.data.roomV2.entities.ClientV2
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDaoV2 {

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insert(clientV2: ClientV2)

    @Query("Select * from clientv2")
    fun getClients(): Flow<List<ClientV2>>

    @Delete
    suspend fun deleteClient(clientV2: ClientV2)

}