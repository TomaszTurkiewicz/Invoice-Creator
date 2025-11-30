package com.tt.invoicecreator.data.roomV2.repository

import com.tt.invoicecreator.data.roomV2.entities.ClientV2
import kotlinx.coroutines.flow.Flow

interface ClientRepositoryV2 {
    fun getAllClients(): Flow<List<ClientV2>>

    suspend fun insertClient(clientV2: ClientV2)

    suspend fun getAllClientsDirectly() : List<ClientV2>

    suspend fun insertWithId(clientV2: ClientV2)

    suspend fun deleteAllClients()
}
