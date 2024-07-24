package com.system.shailendra.core.repositories

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteTable
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import com.system.shailendra.core.entities.FuelHeavyVehicleEntity
import com.system.shailendra.core.entities.FuelTruckEntity
import com.system.shailendra.core.entities.MaterialEntity
import com.system.shailendra.core.entities.StationEntity
import com.system.shailendra.core.repositories.fuel.heavy_vehicle.HeavyVehicleDao
import com.system.shailendra.core.repositories.fuel.truck.TruckFuelDao
import com.system.shailendra.core.repositories.material.MaterialDao
import com.system.shailendra.core.repositories.station.StationDao

@Database(
    entities = [
        MaterialEntity::class,
        FuelTruckEntity::class,
        FuelHeavyVehicleEntity::class,
        StationEntity::class
    ],
    version = 8,
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
