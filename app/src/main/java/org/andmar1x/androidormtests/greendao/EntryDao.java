package org.andmar1x.androidormtests.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import org.andmar1x.androidormtests.greendao.Entry;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ENTRY.
*/
public class EntryDao extends AbstractDao<Entry, Long> {

    public static final String TABLENAME = "ENTRY";

    /**
     * Properties of entity Entry.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property BooleanValue = new Property(1, Boolean.class, "booleanValue", false, "BOOLEAN_VALUE");
        public final static Property BlobValue = new Property(2, byte[].class, "blobValue", false, "BLOB_VALUE");
        public final static Property ShortValue = new Property(3, Short.class, "shortValue", false, "SHORT_VALUE");
        public final static Property IntValue = new Property(4, Integer.class, "intValue", false, "INT_VALUE");
        public final static Property LongValue = new Property(5, Long.class, "longValue", false, "LONG_VALUE");
        public final static Property FloatValue = new Property(6, Float.class, "floatValue", false, "FLOAT_VALUE");
        public final static Property DoubleValue = new Property(7, Double.class, "doubleValue", false, "DOUBLE_VALUE");
        public final static Property StringValue = new Property(8, String.class, "stringValue", false, "STRING_VALUE");
        public final static Property DateValue = new Property(9, java.util.Date.class, "dateValue", false, "DATE_VALUE");
    };


    public EntryDao(DaoConfig config) {
        super(config);
    }
    
    public EntryDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ENTRY' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'BOOLEAN_VALUE' INTEGER," + // 1: booleanValue
                "'BLOB_VALUE' BLOB," + // 2: blobValue
                "'SHORT_VALUE' INTEGER," + // 3: shortValue
                "'INT_VALUE' INTEGER," + // 4: intValue
                "'LONG_VALUE' INTEGER," + // 5: longValue
                "'FLOAT_VALUE' REAL," + // 6: floatValue
                "'DOUBLE_VALUE' REAL," + // 7: doubleValue
                "'STRING_VALUE' TEXT," + // 8: stringValue
                "'DATE_VALUE' INTEGER);"); // 9: dateValue
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ENTRY'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Entry entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Boolean booleanValue = entity.getBooleanValue();
        if (booleanValue != null) {
            stmt.bindLong(2, booleanValue ? 1l: 0l);
        }
 
        byte[] blobValue = entity.getBlobValue();
        if (blobValue != null) {
            stmt.bindBlob(3, blobValue);
        }
 
        Short shortValue = entity.getShortValue();
        if (shortValue != null) {
            stmt.bindLong(4, shortValue);
        }
 
        Integer intValue = entity.getIntValue();
        if (intValue != null) {
            stmt.bindLong(5, intValue);
        }
 
        Long longValue = entity.getLongValue();
        if (longValue != null) {
            stmt.bindLong(6, longValue);
        }
 
        Float floatValue = entity.getFloatValue();
        if (floatValue != null) {
            stmt.bindDouble(7, floatValue);
        }
 
        Double doubleValue = entity.getDoubleValue();
        if (doubleValue != null) {
            stmt.bindDouble(8, doubleValue);
        }
 
        String stringValue = entity.getStringValue();
        if (stringValue != null) {
            stmt.bindString(9, stringValue);
        }
 
        java.util.Date dateValue = entity.getDateValue();
        if (dateValue != null) {
            stmt.bindLong(10, dateValue.getTime());
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Entry readEntity(Cursor cursor, int offset) {
        Entry entity = new Entry( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getShort(offset + 1) != 0, // booleanValue
            cursor.isNull(offset + 2) ? null : cursor.getBlob(offset + 2), // blobValue
            cursor.isNull(offset + 3) ? null : cursor.getShort(offset + 3), // shortValue
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // intValue
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5), // longValue
            cursor.isNull(offset + 6) ? null : cursor.getFloat(offset + 6), // floatValue
            cursor.isNull(offset + 7) ? null : cursor.getDouble(offset + 7), // doubleValue
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // stringValue
            cursor.isNull(offset + 9) ? null : new java.util.Date(cursor.getLong(offset + 9)) // dateValue
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Entry entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setBooleanValue(cursor.isNull(offset + 1) ? null : cursor.getShort(offset + 1) != 0);
        entity.setBlobValue(cursor.isNull(offset + 2) ? null : cursor.getBlob(offset + 2));
        entity.setShortValue(cursor.isNull(offset + 3) ? null : cursor.getShort(offset + 3));
        entity.setIntValue(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setLongValue(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
        entity.setFloatValue(cursor.isNull(offset + 6) ? null : cursor.getFloat(offset + 6));
        entity.setDoubleValue(cursor.isNull(offset + 7) ? null : cursor.getDouble(offset + 7));
        entity.setStringValue(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setDateValue(cursor.isNull(offset + 9) ? null : new java.util.Date(cursor.getLong(offset + 9)));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Entry entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Entry entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}