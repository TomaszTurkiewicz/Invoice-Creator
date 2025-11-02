package com.tt.invoicecreator.data.roomV2.offline_repository

import com.tt.invoicecreator.data.roomV2.dao.ClientDaoV2
import com.tt.invoicecreator.data.roomV2.entities.ClientV2
import com.tt.invoicecreator.data.roomV2.repository.ClientRepositoryV2
import kotlinx.coroutines.flow.Flow

class OfflineClientRepositoryV2 (private val clientDaoV2: ClientDaoV2) : ClientRepositoryV2 {
    override fun getAllClients(): Flow<List<ClientV2>> = clientDaoV2.getClients()

    override suspend fun insertClient(clientV2: ClientV2) = clientDaoV2.insert(clientV2)

}