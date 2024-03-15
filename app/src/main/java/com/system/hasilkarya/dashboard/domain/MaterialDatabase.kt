package com.system.hasilkarya.dashboard.domain

import androidx.room.Database
import androidx.room.RoomDatabase
import com.system.hasilkarya.dashboard.data.MaterialEntity

@Database(
    entities = [MaterialEntity::class],
    version = 1
)
abstract class MaterialDatabase: RoomDatabase() {
    abstract val dao: MaterialDao
}