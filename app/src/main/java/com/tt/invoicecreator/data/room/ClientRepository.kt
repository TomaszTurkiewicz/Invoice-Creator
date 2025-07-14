package com.tt.invoicecreator.data.room

import kotlinx.coroutines.flow.Flow

interface ClientRepository{
    fun getAllClients() : Flow<List<Client>>

    suspend fun insertClient(client: Client)

    suspend fun deleteClient(client: Client)
}