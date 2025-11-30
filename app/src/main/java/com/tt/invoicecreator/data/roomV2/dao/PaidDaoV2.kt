package com.tt.invoicecreator.data.roomV2.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tt.invoicecreator.data.roomV2.entities.PaidV2
import kotlinx.coroutines.flow.Flow

@Dao
interface PaidDaoV2 {

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insert(paidV2: PaidV2)

    @Query("Select * from PaidV2")
    fun getPaid(): Flow<List<PaidV2>>

    @Query("Select * from PaidV2")
    suspend fun getPaidDirectly():List<PaidV2>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertWithId(paidV2: PaidV2)

    @Query("DELETE FROM paidV2")
    suspend fun deleteAll()

}