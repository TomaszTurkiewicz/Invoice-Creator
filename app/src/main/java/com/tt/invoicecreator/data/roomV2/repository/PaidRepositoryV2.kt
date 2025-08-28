package com.tt.invoicecreator.data.roomV2.repository

import com.tt.invoicecreator.data.roomV2.entities.PaidV2
import kotlinx.coroutines.flow.Flow

interface PaidRepositoryV2 {

    fun getAllPaid(): Flow<List<PaidV2>>

    suspend fun insertPaid(paidV2: PaidV2)

}