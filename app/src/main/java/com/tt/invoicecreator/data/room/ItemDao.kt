package com.tt.invoicecreator.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Query("Select * from item")
    fun getItems() : Flow<List<Item>>

    @Delete
    suspend fun deleteItem(item: Item)

}