package com.system.hasilkarya.core.repositories

import androidx.room.Database
import androidx.room.RoomDatabase
import com.system.hasilkarya.core.entities.GasEntity
import com.system.hasilkarya.core.entities.MaterialEntity
import com.system.hasilkarya.dashboard.domain.GasDao
import com.system.hasilkarya.gas.domain.MaterialDao

@Database(
    entities = [MaterialEntity::class, GasEntity::class],
    version = 2
)
abstract class AppDatabase: RoomDatabase() {
    abstract val materialDao: MaterialDao
    abstract val gasDao: GasDao
}