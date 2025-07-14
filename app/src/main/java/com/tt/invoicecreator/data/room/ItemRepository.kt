package com.tt.invoicecreator.data.room

import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    fun getAllItems() : Flow<List<Item>>

    suspend fun insertItem(item: Item)

    suspend fun deleteItem(item: Item)
}