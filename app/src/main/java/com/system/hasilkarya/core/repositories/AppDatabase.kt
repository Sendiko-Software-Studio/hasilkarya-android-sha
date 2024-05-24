package com.system.hasilkarya.core.repositories

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteTable
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import com.system.hasilkarya.core.entities.FuelHeavyVehicleEntity
import com.system.hasilkarya.core.entities.FuelTruckEntity
import com.system.hasilkarya.core.entities.MaterialEntity
import com.system.hasilkarya.core.entities.StationEntity
import com.system.hasilkarya.core.repositories.fuel.heavy_vehicle.HeavyVehicleDao
import com.system.hasilkarya.core.repositories.fuel.truck.TruckFuelDao
import com.system.hasilkarya.core.repositories.material.MaterialDao
import com.system.hasilkarya.core.repositories.station.StationDao

@Database(
    entities = [
        MaterialEntity::class,
        FuelTruckEntity::class,
        FuelHeavyVehicleEntity::class,
        StationEntity::class
    ],
    version = 8,
    autoMigrations = [AutoMigration(from = 7, to = 8)]
)
abstract class AppDatabase : RoomDatabase() {
    abstract val materialDao: MaterialDao
    abstract val truckFuelDao: TruckFuelDao
    abstract val heavyVehicleDao: HeavyVehicleDao
    abstract val stationDao: StationDao
}

@DeleteTable.Entries(
    DeleteTable(tableName = "gas")
)
class Migration23 : AutoMigrationSpec
