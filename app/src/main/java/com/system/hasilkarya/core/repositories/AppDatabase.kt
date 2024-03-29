package com.system.hasilkarya.core.repositories

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteTable
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import com.system.hasilkarya.core.entities.FuelHeavyVehicleEntity
import com.system.hasilkarya.core.entities.FuelTruckEntity
import com.system.hasilkarya.core.entities.MaterialEntity
import com.system.hasilkarya.core.repositories.fuel.heavy_vehicle.HeavyVehicleDao
import com.system.hasilkarya.core.repositories.fuel.truck.TruckFuelDao
import com.system.hasilkarya.core.repositories.material.MaterialDao

@Database(
    entities = [MaterialEntity::class, FuelTruckEntity::class, FuelHeavyVehicleEntity::class],
    version = 4,
    autoMigrations = [AutoMigration(from = 3, to = 4)]
)
abstract class AppDatabase: RoomDatabase() {
    abstract val materialDao: MaterialDao
    abstract val truckFuelDao: TruckFuelDao
    abstract val heavyVehicleDao: HeavyVehicleDao
}

@DeleteTable.Entries(
    DeleteTable(tableName = "gas")
)
class Migration23: AutoMigrationSpec