package com.tt.invoicecreator.data.roomV2.offline_repository

import com.tt.invoicecreator.data.roomV2.dao.ItemDaoV2
import com.tt.invoicecreator.data.roomV2.entities.ItemV2
import com.tt.invoicecreator.data.roomV2.repository.ItemRepositoryV2
import kotlinx.coroutines.flow.Flow

class OfflineItemRepositoryV2 (private val itemDaoV2: ItemDaoV2): ItemRepositoryV2 {

    override fun getAllItems(): Flow<List<ItemV2>> = itemDaoV2.getItems()

    override suspend fun insertItem(itemV2: ItemV2) = itemDaoV2.insert(itemV2)
}