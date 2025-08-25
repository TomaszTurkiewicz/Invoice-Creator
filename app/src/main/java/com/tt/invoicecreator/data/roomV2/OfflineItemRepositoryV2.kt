package com.tt.invoicecreator.data.roomV2

import kotlinx.coroutines.flow.Flow

class OfflineItemRepositoryV2 (private val itemDaoV2: ItemDaoV2): ItemRepositoryV2 {

    override fun getAllItems(): Flow<List<ItemV2>> = itemDaoV2.getItems()

    override suspend fun insertItem(itemV2: ItemV2) = itemDaoV2.insert(itemV2)

    override suspend fun deleteItem(itemV2: ItemV2) = itemDaoV2.deleteItem(itemV2)
}