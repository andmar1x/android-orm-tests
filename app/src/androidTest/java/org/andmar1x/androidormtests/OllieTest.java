package org.andmar1x.androidormtests;

import org.andmar1x.androidormtests.ollie.Entry;
import org.andmar1x.androidormtests.ollie.EntryDb;
import org.andmar1x.androidormtests.test.DatabaseTestCase;

import java.util.Date;
import java.util.List;

import ollie.Ollie;
import ollie.query.Select;

/**
 * Created by andmar1x on 5/1/15.
 */
public class OllieTest extends DatabaseTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Ollie.init(mContext, EntryDb.NAME, EntryDb.VERSION);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        // Delete.from(Entry.class).execute(); not works now. See https://github.com/pardom/Ollie/issues/23
        Ollie.getDatabase().delete(Entry.TABLE_NAME, "", new String[]{});
        Ollie.getDatabase().close();
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
            Ollie.getDatabase().beginTransaction();
        }

        for (int i = 1; i <= Consts.ITEMS_COUNT; ++i) {
            insertOne(i);
        }

        if (isBulk) {
            Ollie.getDatabase().setTransactionSuccessful();
            Ollie.getDatabase().endTransaction();
        }

        List<Entry> results = Select.from(Entry.class).where("booleanValue=1").fetch();

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
