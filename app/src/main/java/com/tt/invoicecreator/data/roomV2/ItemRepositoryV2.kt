package com.tt.invoicecreator.data.roomV2

import kotlinx.coroutines.flow.Flow

interface ItemRepositoryV2 {
    fun getAllItems() : Flow<List<ItemV2>>

    suspend fun insertItem(itemV2: ItemV2)

    suspend fun deleteItem(itemV2: ItemV2)
}