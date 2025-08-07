package com.tt.invoicecreator.data.roomV2

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDaoV2 {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(itemV2: ItemV2)

    @Query("Select * from ItemV2")
    fun getItems() : Flow<List<ItemV2>>

    @Delete
    suspend fun deleteItem(itemV2: ItemV2)
}