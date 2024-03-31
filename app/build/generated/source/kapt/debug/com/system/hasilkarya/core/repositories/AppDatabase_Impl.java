package com.system.hasilkarya.core.repositories;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.system.hasilkarya.core.repositories.fuel.heavy_vehicle.HeavyVehicleDao;
import com.system.hasilkarya.core.repositories.fuel.heavy_vehicle.HeavyVehicleDao_Impl;
import com.system.hasilkarya.core.repositories.fuel.truck.TruckFuelDao;
import com.system.hasilkarya.core.repositories.fuel.truck.TruckFuelDao_Impl;
import com.system.hasilkarya.core.repositories.material.MaterialDao;
import com.system.hasilkarya.core.repositories.material.MaterialDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile MaterialDao _materialDao;

  private volatile TruckFuelDao _truckFuelDao;

  private volatile HeavyVehicleDao _heavyVehicleDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(4) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `material` (`driverId` TEXT NOT NULL, `truckId` TEXT NOT NULL, `stationId` TEXT NOT NULL, `checkerId` TEXT NOT NULL, `ratio` REAL NOT NULL, `remarks` TEXT NOT NULL, PRIMARY KEY(`driverId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `fuel_truck` (`truckId` TEXT NOT NULL, `driverId` TEXT NOT NULL, `stationId` TEXT NOT NULL, `userId` TEXT NOT NULL, `volume` REAL NOT NULL, `odometer` REAL NOT NULL, `remarks` TEXT NOT NULL, PRIMARY KEY(`truckId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `fuel_heavy_vehicle` (`heavyVehicleId` TEXT NOT NULL, `stationId` TEXT NOT NULL, `driverId` TEXT NOT NULL, `gasOperatorId` TEXT NOT NULL, `volume` REAL NOT NULL, `hourmeter` REAL NOT NULL, `remarks` TEXT NOT NULL, PRIMARY KEY(`heavyVehicleId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '9b7e5a1988eb61e023c245d0aebff67a')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `material`");
        db.execSQL("DROP TABLE IF EXISTS `fuel_truck`");
        db.execSQL("DROP TABLE IF EXISTS `fuel_heavy_vehicle`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsMaterial = new HashMap<String, TableInfo.Column>(6);
        _columnsMaterial.put("driverId", new TableInfo.Column("driverId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaterial.put("truckId", new TableInfo.Column("truckId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaterial.put("stationId", new TableInfo.Column("stationId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaterial.put("checkerId", new TableInfo.Column("checkerId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaterial.put("ratio", new TableInfo.Column("ratio", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaterial.put("remarks", new TableInfo.Column("remarks", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMaterial = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMaterial = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMaterial = new TableInfo("material", _columnsMaterial, _foreignKeysMaterial, _indicesMaterial);
        final TableInfo _existingMaterial = TableInfo.read(db, "material");
        if (!_infoMaterial.equals(_existingMaterial)) {
          return new RoomOpenHelper.ValidationResult(false, "material(com.system.hasilkarya.core.entities.MaterialEntity).\n"
                  + " Expected:\n" + _infoMaterial + "\n"
                  + " Found:\n" + _existingMaterial);
        }
        final HashMap<String, TableInfo.Column> _columnsFuelTruck = new HashMap<String, TableInfo.Column>(7);
        _columnsFuelTruck.put("truckId", new TableInfo.Column("truckId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFuelTruck.put("driverId", new TableInfo.Column("driverId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFuelTruck.put("stationId", new TableInfo.Column("stationId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFuelTruck.put("userId", new TableInfo.Column("userId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFuelTruck.put("volume", new TableInfo.Column("volume", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFuelTruck.put("odometer", new TableInfo.Column("odometer", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFuelTruck.put("remarks", new TableInfo.Column("remarks", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFuelTruck = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFuelTruck = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFuelTruck = new TableInfo("fuel_truck", _columnsFuelTruck, _foreignKeysFuelTruck, _indicesFuelTruck);
        final TableInfo _existingFuelTruck = TableInfo.read(db, "fuel_truck");
        if (!_infoFuelTruck.equals(_existingFuelTruck)) {
          return new RoomOpenHelper.ValidationResult(false, "fuel_truck(com.system.hasilkarya.core.entities.FuelTruckEntity).\n"
                  + " Expected:\n" + _infoFuelTruck + "\n"
                  + " Found:\n" + _existingFuelTruck);
        }
        final HashMap<String, TableInfo.Column> _columnsFuelHeavyVehicle = new HashMap<String, TableInfo.Column>(7);
        _columnsFuelHeavyVehicle.put("heavyVehicleId", new TableInfo.Column("heavyVehicleId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFuelHeavyVehicle.put("stationId", new TableInfo.Column("stationId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFuelHeavyVehicle.put("driverId", new TableInfo.Column("driverId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFuelHeavyVehicle.put("gasOperatorId", new TableInfo.Column("gasOperatorId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFuelHeavyVehicle.put("volume", new TableInfo.Column("volume", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFuelHeavyVehicle.put("hourmeter", new TableInfo.Column("hourmeter", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFuelHeavyVehicle.put("remarks", new TableInfo.Column("remarks", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFuelHeavyVehicle = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFuelHeavyVehicle = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFuelHeavyVehicle = new TableInfo("fuel_heavy_vehicle", _columnsFuelHeavyVehicle, _foreignKeysFuelHeavyVehicle, _indicesFuelHeavyVehicle);
        final TableInfo _existingFuelHeavyVehicle = TableInfo.read(db, "fuel_heavy_vehicle");
        if (!_infoFuelHeavyVehicle.equals(_existingFuelHeavyVehicle)) {
          return new RoomOpenHelper.ValidationResult(false, "fuel_heavy_vehicle(com.system.hasilkarya.core.entities.FuelHeavyVehicleEntity).\n"
                  + " Expected:\n" + _infoFuelHeavyVehicle + "\n"
                  + " Found:\n" + _existingFuelHeavyVehicle);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "9b7e5a1988eb61e023c245d0aebff67a", "f0bfd56b0f08706bc78fdc6d65c7ba5d");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "material","fuel_truck","fuel_heavy_vehicle");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `material`");
      _db.execSQL("DELETE FROM `fuel_truck`");
      _db.execSQL("DELETE FROM `fuel_heavy_vehicle`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(MaterialDao.class, MaterialDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TruckFuelDao.class, TruckFuelDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(HeavyVehicleDao.class, HeavyVehicleDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    _autoMigrations.add(new AppDatabase_AutoMigration_3_4_Impl());
    return _autoMigrations;
  }

  @Override
  public MaterialDao getMaterialDao() {
    if (_materialDao != null) {
      return _materialDao;
    } else {
      synchronized(this) {
        if(_materialDao == null) {
          _materialDao = new MaterialDao_Impl(this);
        }
        return _materialDao;
      }
    }
  }

  @Override
  public TruckFuelDao getTruckFuelDao() {
    if (_truckFuelDao != null) {
      return _truckFuelDao;
    } else {
      synchronized(this) {
        if(_truckFuelDao == null) {
          _truckFuelDao = new TruckFuelDao_Impl(this);
        }
        return _truckFuelDao;
      }
    }
  }

  @Override
  public HeavyVehicleDao getHeavyVehicleDao() {
    if (_heavyVehicleDao != null) {
      return _heavyVehicleDao;
    } else {
      synchronized(this) {
        if(_heavyVehicleDao == null) {
          _heavyVehicleDao = new HeavyVehicleDao_Impl(this);
        }
        return _heavyVehicleDao;
      }
    }
  }
}
