package com.tt.invoicecreator.data.roomV2.offline_repository

import com.tt.invoicecreator.data.roomV2.dao.PaidDaoV2
import com.tt.invoicecreator.data.roomV2.entities.PaidV2
import com.tt.invoicecreator.data.roomV2.repository.PaidRepositoryV2
import kotlinx.coroutines.flow.Flow

class OfflinePaidRepositoryV2 (private val paidDaoV2: PaidDaoV2) : PaidRepositoryV2 {

    override fun getAllPaid(): Flow<List<PaidV2>> = paidDaoV2.getPaid()

    override suspend fun insertPaid(paidV2: PaidV2) = paidDaoV2.insert(paidV2)
    override suspend fun getAllPaidDirectly(): List<PaidV2> = paidDaoV2.getPaidDirectly()

    override suspend fun insertWithId(paidV2: PaidV2) = paidDaoV2.insertWithId(paidV2)

    override suspend fun deleteAllPaid() = paidDaoV2.deleteAll()



}