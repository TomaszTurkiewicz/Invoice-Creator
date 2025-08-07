package com.tt.invoicecreator.data.roomV2

import kotlinx.coroutines.flow.Flow


interface ClientRepositoryV2 {
    fun getAllClients(): Flow<List<ClientV2>>

    suspend fun insertClient(clientV2: ClientV2)

    suspend fun deleteClient(clientV2: ClientV2)

}