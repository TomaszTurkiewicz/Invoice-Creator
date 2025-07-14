package com.tt.invoicecreator.data.room

import kotlinx.coroutines.flow.Flow

class OfflineItemRepository (private val itemDao: ItemDao): ItemRepository{
    override fun getAllItems(): Flow<List<Item>> = itemDao.getItems()

    override suspend fun insertItem(item: Item) = itemDao.insert(item)

    override suspend fun deleteItem(item: Item) = itemDao.deleteItem(item)
}