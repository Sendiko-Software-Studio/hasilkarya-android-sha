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

import com.system.hasilkarya.core.repositories.fuel.truck.TruckFuelDao;
import com.system.hasilkarya.core.repositories.material.MaterialDao;

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

  private volatile TruckFuelDao _Truck_fuelDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `material` (`driverId` TEXT NOT NULL, `truckId` TEXT NOT NULL, `stationId` TEXT NOT NULL, `checkerId` TEXT NOT NULL, `ratio` REAL NOT NULL, `remarks` TEXT NOT NULL, PRIMARY KEY(`driverId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `fuel_truck` (`truckId` TEXT NOT NULL, `driverId` TEXT NOT NULL, `stationId` TEXT NOT NULL, `userId` TEXT NOT NULL, `volume` REAL NOT NULL, `odometer` REAL NOT NULL, `remarks` TEXT NOT NULL, PRIMARY KEY(`truckId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '7a9500de8b89097361971e2dad19dde2')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `material`");
        db.execSQL("DROP TABLE IF EXISTS `fuel_truck`");
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
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "7a9500de8b89097361971e2dad19dde2", "e4c208c8cf3730f905ae102e83523518");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "material","fuel_truck");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `material`");
      _db.execSQL("DELETE FROM `fuel_truck`");
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
    _autoMigrations.add(new AppDatabase_AutoMigration_2_3_Impl());
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
    if (_Truck_fuelDao != null) {
      return _Truck_fuelDao;
    } else {
      synchronized(this) {
        if(_Truck_fuelDao == null) {
          _Truck_fuelDao = new TruckFuelDao_Impl(this);
        }
        return _Truck_fuelDao;
      }
    }
  }
}
