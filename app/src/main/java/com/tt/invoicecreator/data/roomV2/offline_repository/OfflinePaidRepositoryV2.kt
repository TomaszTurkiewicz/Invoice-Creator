package com.tt.invoicecreator.data.roomV2.offline_repository

import com.tt.invoicecreator.data.roomV2.dao.PaidDaoV2
import com.tt.invoicecreator.data.roomV2.entities.PaidV2
import com.tt.invoicecreator.data.roomV2.repository.PaidRepositoryV2
import kotlinx.coroutines.flow.Flow

class OfflinePaidRepositoryV2 (private val paidDaoV2: PaidDaoV2) : PaidRepositoryV2 {

    override fun getAllPaid(): Flow<List<PaidV2>> = paidDaoV2.getPaid()

    override suspend fun insertPaid(paidV2: PaidV2) = paidDaoV2.insert(paidV2)
}