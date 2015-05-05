package org.andmar1x.androidormtests.ormlite;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;

import org.andmar1x.androidormtests.Consts;
import org.andmar1x.androidormtests.test.SimpleTestCase;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by andmar1x on 5/4/15.
 */
public class SimpleTest extends SimpleTestCase {

    private EntryDbHelper mEntryDbHelper;
    private Dao<Entry, Long> mEntryDao;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mEntryDbHelper = new EntryDbHelper(mContext);
        mEntryDao = mEntryDbHelper.getEntryDao();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        mEntryDbHelper.clearTable();
        mEntryDbHelper.close();
    }

    @Override
    public void testSingleInsert() {
        insert(false);
    }

    @Override
    public void testBulkInsert() {
        insert(true);
    }

    private void insert(boolean isBulk) {
        if (isBulk) {
            try {
                TransactionManager.callInTransaction(mEntryDao.getConnectionSource(), new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        for (int i = 1; i <= Consts.ITEMS_COUNT; ++i) {
                            insertOne(i);
                        }
                        return null;
                    }
                });
            } catch (SQLException e) {
                e.printStackTrace();
                fail();
            }
        } else {
            for (int i = 1; i <= Consts.ITEMS_COUNT; ++i) {
                try {
                    insertOne(i);
                } catch (SQLException e) {
                    e.printStackTrace();
                    fail();
                }
            }
        }

        try {
            List<Entry> results = mEntryDao.query(mEntryDao.queryBuilder().where().eq("booleanValue", true).prepare());

            assertEquals(Consts.ITEMS_COUNT / 2, results.size());
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    private void insertOne(int i) throws SQLException {
        Entry entry = new Entry();
        entry.booleanValue = (i % 2 == 0);
        entry.shortValue = (short) i;
        entry.intValue = i;
        entry.floatValue = (float) i;
        entry.doubleValue = (double) i;
        entry.stringValue = Entry.class.getSimpleName() + " " + i;
        entry.dateValue = new Date();

        mEntryDao.create(entry);
    }
}
