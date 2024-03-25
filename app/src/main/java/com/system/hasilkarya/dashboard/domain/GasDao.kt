package com.system.hasilkarya.dashboard.domain

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.system.hasilkarya.core.entities.GasEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GasDao {

    @Upsert
    suspend fun saveGas(gasEntity: GasEntity)

    @Query("SELECT * FROM gas")
    fun getGases(): Flow<List<GasEntity>>

    @Delete
    fun deleteGas(gasEntity: GasEntity)
}