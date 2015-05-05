package org.andmar1x.androidormtests.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.android.AndroidDatabaseConnection;
import com.j256.ormlite.android.DatabaseTableConfigUtil;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by andmar1x on 5/4/15.
 */
public class EntryDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ormlite_db.db";
    private static final int DATABASE_VERSION = 1;

    protected AndroidConnectionSource mConnectionSource = new AndroidConnectionSource(this);

    private Dao<Entry, Long> mDao = null;

    public EntryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DatabaseConnection conn = mConnectionSource.getSpecialConnection();
        boolean clearSpecial = false;
        if (conn == null) {
            conn = new AndroidDatabaseConnection(db, true);
            try {
                mConnectionSource.saveSpecialConnection(conn);
                clearSpecial = true;
            } catch (SQLException e) {
                throw new IllegalStateException("Could not save special connection", e);
            }
        }
        try {
            onCreate();
        } finally {
            if (clearSpecial) {
                mConnectionSource.clearSpecialConnection(conn);
            }
        }
    }

    @Override
    public final void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        DatabaseConnection conn = mConnectionSource.getSpecialConnection();
        boolean clearSpecial = false;
        if (conn == null) {
            conn = new AndroidDatabaseConnection(db, true);
            try {
                mConnectionSource.saveSpecialConnection(conn);
                clearSpecial = true;
            } catch (SQLException e) {
                throw new IllegalStateException("Could not save special connection", e);
            }
        }
        try {
            onUpgrade(oldVersion, newVersion);
        } finally {
            if (clearSpecial) {
                mConnectionSource.clearSpecialConnection(conn);
            }
        }
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        mDao = null;
    }

    public Dao<Entry, Long> getEntryDao() throws SQLException {
        if (mDao == null) {
            mDao = getEntryDao(Entry.class);
        }
        return mDao;
    }

    public void clearTable() throws SQLException {
        TableUtils.dropTable(mConnectionSource, Entry.class, true);
        TableUtils.createTable(mConnectionSource, Entry.class);
    }

    private void onCreate() {
        try {
            TableUtils.createTable(mConnectionSource, Entry.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void onUpgrade(int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(mConnectionSource, Entry.class, true);
            onCreate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private <D extends Dao<T, ?>, T> D getEntryDao(Class<T> clazz) throws SQLException {
        Dao<T, ?> dao = DaoManager.lookupDao(mConnectionSource, clazz);
        if (dao == null) {
            DatabaseTableConfig<T> tableConfig = DatabaseTableConfigUtil.fromClass(mConnectionSource, clazz);
            if (tableConfig == null) {
                dao = DaoManager.createDao(mConnectionSource, clazz);
            } else {
                dao = DaoManager.createDao(mConnectionSource, tableConfig);
            }
        }

        @SuppressWarnings("unchecked")
        D castDao = (D) dao;
        return castDao;
    }
}
