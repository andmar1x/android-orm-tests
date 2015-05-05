package org.andmar1x.androidormtests.simple;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.andmar1x.androidormtests.Consts;
import org.andmar1x.androidormtests.dbflow.Entry;
import org.andmar1x.androidormtests.dbflow.Entry$Table;
import org.andmar1x.androidormtests.dbflow.EntryDb;
import org.andmar1x.androidormtests.test.SimpleTestCase;

import java.util.Date;
import java.util.List;

/**
 * Created by andmar1x on 5/4/15.
 */
public class DbFlowSimpleTest extends SimpleTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        FlowManager.init(mContext);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        // Clear database
        new Delete().from(Entry.class).query();
        FlowManager.destroy();
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
            TransactionManager.transact(EntryDb.NAME, new Runnable() {
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

        List<Entry> results = new Select().from(Entry.class)
                .where(Condition.column(Entry$Table.BOOLEANVALUE).eq("1")).queryList();

        assertEquals(Consts.ITEMS_COUNT / 2, results.size());
    }

    private void insertOne(int i) {
        Entry entry = new Entry();
        entry.booleanValue = (i % 2 == 0);
        entry.shortValue = (short) i;
        entry.intValue = i;
        entry.floatValue = (float) i;
        entry.doubleValue = (double) i;
        entry.stringValue = Entry.class.getSimpleName() + " " + i;
        entry.dateValue = new Date();

        entry.insert(false);
    }
}
