package com.system.hasilkarya.core.repositories

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteTable
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import com.system.hasilkarya.core.entities.FuelTruckEntity
import com.system.hasilkarya.core.entities.MaterialEntity
import com.system.hasilkarya.core.repositories.fuel.truck.TruckFuelDao
import com.system.hasilkarya.core.repositories.material.MaterialDao

@Database(
    entities = [MaterialEntity::class, FuelTruckEntity::class],
    version = 3,
    autoMigrations = [AutoMigration(from = 2, to = 3, Migration23::class)]
)
abstract class AppDatabase: RoomDatabase() {
    abstract val materialDao: MaterialDao
    abstract val truckFuelDao: TruckFuelDao
}

@DeleteTable.Entries(
    DeleteTable(tableName = "gas")
)
class Migration23: AutoMigrationSpec