package com.system.hasilkarya.core.repositories;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.EntityUpsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.system.hasilkarya.core.entities.FuelTruckEntity;
import com.system.hasilkarya.core.repositories.fuel.truck.TruckFuelDao;

import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@SuppressWarnings({"unchecked", "deprecation"})
public final class TruckFuelDao_Impl implements TruckFuelDao {
  private final RoomDatabase __db;

  private final EntityDeletionOrUpdateAdapter<FuelTruckEntity> __deletionAdapterOfFuelTruckEntity;

  private final EntityUpsertionAdapter<FuelTruckEntity> __upsertionAdapterOfFuelTruckEntity;

  public TruckFuelDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__deletionAdapterOfFuelTruckEntity = new EntityDeletionOrUpdateAdapter<FuelTruckEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `fuel_truck` WHERE `truckId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FuelTruckEntity entity) {
        if (entity.getTruckId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getTruckId());
        }
      }
    };
    this.__upsertionAdapterOfFuelTruckEntity = new EntityUpsertionAdapter<FuelTruckEntity>(new EntityInsertionAdapter<FuelTruckEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT INTO `fuel_truck` (`truckId`,`driverId`,`stationId`,`userId`,`volume`,`odometer`,`remarks`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FuelTruckEntity entity) {
        if (entity.getTruckId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getTruckId());
        }
        if (entity.getDriverId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getDriverId());
        }
        if (entity.getStationId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getStationId());
        }
        if (entity.getUserId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getUserId());
        }
        statement.bindDouble(5, entity.getVolume());
        statement.bindDouble(6, entity.getOdometer());
        if (entity.getRemarks() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getRemarks());
        }
      }
    }, new EntityDeletionOrUpdateAdapter<FuelTruckEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE `fuel_truck` SET `truckId` = ?,`driverId` = ?,`stationId` = ?,`userId` = ?,`volume` = ?,`odometer` = ?,`remarks` = ? WHERE `truckId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FuelTruckEntity entity) {
        if (entity.getTruckId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getTruckId());
        }
        if (entity.getDriverId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getDriverId());
        }
        if (entity.getStationId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getStationId());
        }
        if (entity.getUserId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getUserId());
        }
        statement.bindDouble(5, entity.getVolume());
        statement.bindDouble(6, entity.getOdometer());
        if (entity.getRemarks() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getRemarks());
        }
        if (entity.getTruckId() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getTruckId());
        }
      }
    });
  }

  @Override
  public Object deleteFuel(final FuelTruckEntity fuelTruckEntity,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfFuelTruckEntity.handle(fuelTruckEntity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object saveFuel(final FuelTruckEntity fuelTruckEntity,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfFuelTruckEntity.upsert(fuelTruckEntity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<FuelTruckEntity>> getFuels() {
    final String _sql = "SELECT * FROM fuel_truck";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"fuel_truck"}, new Callable<List<FuelTruckEntity>>() {
      @Override
      @NonNull
      public List<FuelTruckEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTruckId = CursorUtil.getColumnIndexOrThrow(_cursor, "truckId");
          final int _cursorIndexOfDriverId = CursorUtil.getColumnIndexOrThrow(_cursor, "driverId");
          final int _cursorIndexOfStationId = CursorUtil.getColumnIndexOrThrow(_cursor, "stationId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfVolume = CursorUtil.getColumnIndexOrThrow(_cursor, "volume");
          final int _cursorIndexOfOdometer = CursorUtil.getColumnIndexOrThrow(_cursor, "odometer");
          final int _cursorIndexOfRemarks = CursorUtil.getColumnIndexOrThrow(_cursor, "remarks");
          final List<FuelTruckEntity> _result = new ArrayList<FuelTruckEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FuelTruckEntity _item;
            final String _tmpTruckId;
            if (_cursor.isNull(_cursorIndexOfTruckId)) {
              _tmpTruckId = null;
            } else {
              _tmpTruckId = _cursor.getString(_cursorIndexOfTruckId);
            }
            final String _tmpDriverId;
            if (_cursor.isNull(_cursorIndexOfDriverId)) {
              _tmpDriverId = null;
            } else {
              _tmpDriverId = _cursor.getString(_cursorIndexOfDriverId);
            }
            final String _tmpStationId;
            if (_cursor.isNull(_cursorIndexOfStationId)) {
              _tmpStationId = null;
            } else {
              _tmpStationId = _cursor.getString(_cursorIndexOfStationId);
            }
            final String _tmpUserId;
            if (_cursor.isNull(_cursorIndexOfUserId)) {
              _tmpUserId = null;
            } else {
              _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            }
            final double _tmpVolume;
            _tmpVolume = _cursor.getDouble(_cursorIndexOfVolume);
            final double _tmpOdometer;
            _tmpOdometer = _cursor.getDouble(_cursorIndexOfOdometer);
            final String _tmpRemarks;
            if (_cursor.isNull(_cursorIndexOfRemarks)) {
              _tmpRemarks = null;
            } else {
              _tmpRemarks = _cursor.getString(_cursorIndexOfRemarks);
            }
            _item = new FuelTruckEntity(_tmpTruckId,_tmpDriverId,_tmpStationId,_tmpUserId,_tmpVolume,_tmpOdometer,_tmpRemarks);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
