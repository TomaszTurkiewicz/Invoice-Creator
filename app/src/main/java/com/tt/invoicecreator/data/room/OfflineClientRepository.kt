package com.tt.invoicecreator.data.room

import kotlinx.coroutines.flow.Flow

class OfflineClientRepository (private val clientDao: ClientDao) : ClientRepository {
    override fun getAllClients(): Flow<List<Client>> = clientDao.getClients()

    override suspend fun insertClient(client: Client) = clientDao.insert(client)

    override suspend fun deleteClient(client: Client) = clientDao.deleteClient(client)

}