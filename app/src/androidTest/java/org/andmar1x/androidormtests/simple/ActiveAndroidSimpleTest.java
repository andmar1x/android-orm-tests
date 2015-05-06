package org.andmar1x.androidormtests.simple;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import org.andmar1x.androidormtests.Consts;
import org.andmar1x.androidormtests.activeandroid.Entry;
import org.andmar1x.androidormtests.test.SimpleTestCase;

import java.util.Date;
import java.util.List;

/**
 * Created by andmar1x on 5/1/15.
 */
public class ActiveAndroidSimpleTest extends SimpleTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        ActiveAndroid.initialize(mContext);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        new Delete().from(Entry.class).execute();
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
            ActiveAndroid.beginTransaction();
        }

        for (int i = 1; i <= Consts.ITEMS_COUNT; ++i) {
            insertOne(i);
        }

        if (isBulk) {
            ActiveAndroid.setTransactionSuccessful();
            ActiveAndroid.endTransaction();
        }

        List<Entry> results = new Select().from(Entry.class).where("booleanValue=1").execute();

        assertEquals(Consts.ITEMS_COUNT / 2, results.size());
    }

    private void insertOne(int i) {
        Entry entry = new Entry();
        entry.booleanValue = (i % 2 == 0);
        entry.shortValue = (short) i;
        entry.intValue = i;
        entry.longValue = (long) i;
        entry.floatValue = (float) i;
        entry.doubleValue = (double) i;
        entry.stringValue = Entry.class.getSimpleName() + " " + i;
        entry.dateValue = new Date();

        entry.save();
    }
}
