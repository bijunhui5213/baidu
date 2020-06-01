package com.lnsoft.gmvpn.seconnect.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.lnsoft.gmvpn.seconnect.bean.DbBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DB_BEAN".
*/
public class DbBeanDao extends AbstractDao<DbBean, Long> {

    public static final String TABLENAME = "DB_BEAN";

    /**
     * Properties of entity DbBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property AppName = new Property(1, String.class, "AppName", false, "APP_NAME");
        public final static Property PackageName = new Property(2, String.class, "PackageName", false, "PACKAGE_NAME");
        public final static Property VersionName = new Property(3, String.class, "VersionName", false, "VERSION_NAME");
        public final static Property VersionCode = new Property(4, int.class, "VersionCode", false, "VERSION_CODE");
        public final static Property AppIcon = new Property(5, Integer.class, "AppIcon", false, "APP_ICON");
        public final static Property IsCheck = new Property(6, boolean.class, "isCheck", false, "IS_CHECK");
    }


    public DbBeanDao(DaoConfig config) {
        super(config);
    }
    
    public DbBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DB_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"APP_NAME\" TEXT," + // 1: AppName
                "\"PACKAGE_NAME\" TEXT," + // 2: PackageName
                "\"VERSION_NAME\" TEXT," + // 3: VersionName
                "\"VERSION_CODE\" INTEGER NOT NULL ," + // 4: VersionCode
                "\"APP_ICON\" INTEGER," + // 5: AppIcon
                "\"IS_CHECK\" INTEGER NOT NULL );"); // 6: isCheck
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DB_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, DbBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String AppName = entity.getAppName();
        if (AppName != null) {
            stmt.bindString(2, AppName);
        }
 
        String PackageName = entity.getPackageName();
        if (PackageName != null) {
            stmt.bindString(3, PackageName);
        }
 
        String VersionName = entity.getVersionName();
        if (VersionName != null) {
            stmt.bindString(4, VersionName);
        }
        stmt.bindLong(5, entity.getVersionCode());
 
        Integer AppIcon = entity.getAppIcon();
        if (AppIcon != null) {
            stmt.bindLong(6, AppIcon);
        }
        stmt.bindLong(7, entity.getIsCheck() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, DbBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String AppName = entity.getAppName();
        if (AppName != null) {
            stmt.bindString(2, AppName);
        }
 
        String PackageName = entity.getPackageName();
        if (PackageName != null) {
            stmt.bindString(3, PackageName);
        }
 
        String VersionName = entity.getVersionName();
        if (VersionName != null) {
            stmt.bindString(4, VersionName);
        }
        stmt.bindLong(5, entity.getVersionCode());
 
        Integer AppIcon = entity.getAppIcon();
        if (AppIcon != null) {
            stmt.bindLong(6, AppIcon);
        }
        stmt.bindLong(7, entity.getIsCheck() ? 1L: 0L);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public DbBean readEntity(Cursor cursor, int offset) {
        DbBean entity = new DbBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // AppName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // PackageName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // VersionName
            cursor.getInt(offset + 4), // VersionCode
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // AppIcon
            cursor.getShort(offset + 6) != 0 // isCheck
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, DbBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setAppName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPackageName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setVersionName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setVersionCode(cursor.getInt(offset + 4));
        entity.setAppIcon(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setIsCheck(cursor.getShort(offset + 6) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(DbBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(DbBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(DbBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
