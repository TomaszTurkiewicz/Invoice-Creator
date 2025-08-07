package com.tt.invoicecreator.data.roomV2

import kotlinx.coroutines.flow.Flow

class OfflineClientRepositoryV2 (private val clientDaoV2: ClientDaoV2) : ClientRepositoryV2{
    override fun getAllClients(): Flow<List<ClientV2>> = clientDaoV2.getClients()

    override suspend fun insertClient(clientV2: ClientV2) = clientDaoV2.insert(clientV2)

    override suspend fun deleteClient(clientV2: ClientV2) = clientDaoV2.deleteClient(clientV2)

}