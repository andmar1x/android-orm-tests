package org.andmar1x.androidormtests.simple;

import android.database.sqlite.SQLiteDatabase;

import org.andmar1x.androidormtests.Consts;
import org.andmar1x.androidormtests.greendao.DaoMaster;
import org.andmar1x.androidormtests.greendao.DaoSession;
import org.andmar1x.androidormtests.greendao.Entry;
import org.andmar1x.androidormtests.greendao.EntryDao;
import org.andmar1x.androidormtests.greendao.EntryDb;
import org.andmar1x.androidormtests.test.SimpleTestCase;

import java.util.Date;
import java.util.List;

/**
 * Created by andmar1x on 5/4/15.
 */
public class GreenDaoSimpleTest extends SimpleTestCase {

    private DaoSession mDaoSession;
    private EntryDao mEntryDao;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        SQLiteDatabase db = new DaoMaster.DevOpenHelper(mContext, EntryDb.NAME, null).getWritableDatabase();
        mDaoSession = new DaoMaster(db).newSession();
        mEntryDao = mDaoSession.getEntryDao();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        mEntryDao.deleteAll();
        mDaoSession.getDatabase().close();
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
            mDaoSession.runInTx(new Runnable() {
                @Override
                public void run() {
                    for (int i = 1; i <= Consts.ITEMS_COUNT; ++i) {
                        insertOne(i);
                    }
                }
            });
        } else {
            for (int i = 1; i <= Consts.ITEMS_COUNT; ++i) {
                insertOne(i);
            }
        }

        List<Entry> results = mEntryDao.queryBuilder().where(EntryDao.Properties.BooleanValue.eq("TRUE")).list();

        assertEquals(Consts.ITEMS_COUNT / 2, results.size());
    }

    private void insertOne(int i) {
        Entry entry = new Entry();
        entry.setBooleanValue(i % 2 == 0);
        entry.setShortValue((short) i);
        entry.setIntValue(i);
        entry.setLongValue((long) i);
        entry.setFloatValue((float) i);
        entry.setDoubleValue((double) i);
        entry.setStringValue(Entry.class.getSimpleName() + " " + i);
        entry.setDateValue(new Date());

        mEntryDao.insert(entry);
    }
}
