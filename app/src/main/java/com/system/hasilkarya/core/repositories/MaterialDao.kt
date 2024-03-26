package com.system.hasilkarya.core.repositories

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.system.hasilkarya.core.entities.MaterialEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MaterialDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMaterial(material: MaterialEntity)

    @Query("SELECT * FROM material")
    fun getAllMaterial(): Flow<List<MaterialEntity>>

    @Delete
    suspend fun deleteMaterial(material: MaterialEntity)
}