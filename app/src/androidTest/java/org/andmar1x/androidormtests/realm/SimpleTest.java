package org.andmar1x.androidormtests.realm;

import org.andmar1x.androidormtests.Consts;
import org.andmar1x.androidormtests.test.SimpleTestCase;

import java.util.Date;
import java.util.List;

import io.realm.Realm;

/**
 * Created by andmar1x on 5/1/15.
 */
public class SimpleTest extends SimpleTestCase {

    protected Realm mRealm;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Realm.deleteRealmFile(mContext);
        mRealm = Realm.getInstance(mContext);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        if (mRealm != null) {
            mRealm.close();
        }
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
            mRealm.beginTransaction();
        }

        for (int i = 1; i <= Consts.ITEMS_COUNT; ++i) {
            if (!isBulk) {
                mRealm.beginTransaction();
            }

            insertOne(i);

            if (!isBulk) {
                mRealm.commitTransaction();
            }
        }

        if (isBulk) {
            mRealm.commitTransaction();
        }

        List<Entry> results = mRealm.where(Entry.class).equalTo("booleanValue", true).findAll();

        assertEquals(Consts.ITEMS_COUNT / 2, results.size());
    }

    private void insertOne(int i) {
        Entry entry = mRealm.createObject(Entry.class);
        entry.setBooleanValue(i % 2 == 0);
        entry.setShortValue((short) i);
        entry.setIntValue(i);
        entry.setLongValue(i);
        entry.setFloatValue(i);
        entry.setDoubleValue(i);
        entry.setStringValue(Entry.class.getSimpleName() + " " + i);
        entry.setDateValue(new Date());
    }
}
