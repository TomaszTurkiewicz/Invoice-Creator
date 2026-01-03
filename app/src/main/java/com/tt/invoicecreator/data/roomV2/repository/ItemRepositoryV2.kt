package com.tt.invoicecreator.data.roomV2.repository

import com.tt.invoicecreator.data.roomV2.entities.ItemV2
import kotlinx.coroutines.flow.Flow

interface ItemRepositoryV2 {
    fun getAllItems() : Flow<List<ItemV2>>

    suspend fun insertItem(itemV2: ItemV2)

    suspend fun getAllItemsDirectly() : List<ItemV2>

    suspend fun insertWithId(itemV2: ItemV2)

    suspend fun deleteAllItems()

    suspend fun update(itemV2: ItemV2)


}