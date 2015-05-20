package org.andmar1x.androidormtests.simple;

import android.support.annotation.Nullable;
import android.test.AndroidTestCase;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.runtime.transaction.BaseTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.TransactionListener;
import com.raizlabs.android.dbflow.runtime.transaction.process.InsertModelTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelInfo;
import com.raizlabs.android.dbflow.runtime.transaction.process.UpdateModelListTransaction;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.andmar1x.androidormtests.Consts;
import org.andmar1x.androidormtests.dbflow.Entry;
import org.andmar1x.androidormtests.dbflow.Entry$Table;
import org.andmar1x.androidormtests.dbflow.Entry1;
import org.andmar1x.androidormtests.dbflow.Entry2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by andmar1x on 5/4/15.
 */
public class DbFlowComplexTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        FlowManager.init(mContext);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        // Clear databases
        new Delete().from(Entry.class).query();
        new Delete().from(Entry1.class).query();
        new Delete().from(Entry2.class).query();
        FlowManager.destroy();
    }

    public void testInsertToOneTable() throws Throwable {
        final CountDownLatch countDownLatch = new CountDownLatch(2);
        final EntryCreator<Entry> entryCreator = new EntryCreator<>(Entry.class);
        new Thread() {
            @Override
            public void run() {
                try {
                    entryCreator.createInsertTransaction(countDownLatch);
                } catch (Exception e) {
                    fail();
                }
            }
        }.start();

        try {
            entryCreator.createInsertTransaction(countDownLatch);
            countDownLatch.await();
        } catch (Exception e) {
            fail();
        }

        long count = new Select().count().from(Entry.class).count();

        assertEquals(Consts.ITEMS_COUNT * 2, count);
    }

    public void testInsertToTwoTables() throws Throwable {
        final CountDownLatch countDownLatch = new CountDownLatch(2);
        final EntryCreator<Entry1> entryCreator1 = new EntryCreator<>(Entry1.class);
        new Thread() {
            @Override
            public void run() {
                try {
                    entryCreator1.createInsertTransaction(countDownLatch);
                } catch (Exception e) {
                    fail();
                }
            }
        }.start();

        EntryCreator<Entry2> entryCreator2 = new EntryCreator<>(Entry2.class);
        try {
            entryCreator2.createInsertTransaction(countDownLatch);
            countDownLatch.await();
        } catch (InterruptedException e) {
            fail();
        }

        long count1 = new Select().count().from(Entry1.class).count();
        long count2 = new Select().count().from(Entry2.class).count();

        assertEquals(Consts.ITEMS_COUNT * 2, count1 + count2);
    }

    public void testInsertToOneTableAndUpdateAnother() throws Throwable {
        final CountDownLatch countDownLatch = new CountDownLatch(3);

        final EntryCreator<Entry2> entryCreator2 = new EntryCreator<>(Entry2.class);
        entryCreator2.createInsertTransaction(countDownLatch);

        final EntryCreator<Entry1> entryCreator1 = new EntryCreator<>(Entry1.class);
        new Thread() {
            @Override
            public void run() {
                try {
                    entryCreator1.createInsertTransaction(countDownLatch);
                } catch (Exception e) {
                    fail();
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                try {
                    entryCreator2.createUpdateTransaction(countDownLatch);
                } catch (Exception e) {
                    fail();
                }
            }
        }.start();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            fail();
        }

        long countTrue = new Select().count().from(Entry2.class)
                .where(Condition.column(Entry$Table.BOOLEANVALUE).eq("1")).count();

        assertEquals(Consts.ITEMS_COUNT, countTrue);

        long count1 = new Select().count().from(Entry1.class).count();
        long count2 = new Select().count().from(Entry2.class).count();

        assertEquals(Consts.ITEMS_COUNT * 2, count1 + count2);
    }

    private class EntryCreator<T extends Entry> {

        private final Class<T> mHandlerClass;

        public EntryCreator(final Class<T> handlerClass) {
            mHandlerClass = handlerClass;
        }

        public T create(int val) throws IllegalAccessException, InstantiationException {
            T entry = mHandlerClass.newInstance();
            entry.booleanValue = (val % 2 == 0);
            entry.shortValue = (short) val;
            entry.intValue = val;
            entry.floatValue = (float) val;
            entry.doubleValue = (double) val;
            entry.stringValue = " " + val;
            entry.dateValue = new Date();
            return entry;
        }

        public void createInsertTransaction(@Nullable final CountDownLatch countDownLatch)
                throws InstantiationException, IllegalAccessException {
            List<T> entries = new ArrayList<>();
            for (int i = 1; i <= Consts.ITEMS_COUNT; ++i) {
                entries.add(create(i));
            }

            ProcessModelInfo<T> processModelInfo = ProcessModelInfo.withModels(entries)
                    .result(new TransactionListener<List<T>>() {
                        @Override
                        public void onResultReceived(List<T> ts) {
                            if (countDownLatch != null) {
                                countDownLatch.countDown();
                            }
                        }

                        @Override
                        public boolean onReady(BaseTransaction<List<T>> baseTransaction) {
                            return true;
                        }

                        @Override
                        public boolean hasResult(BaseTransaction<List<T>> baseTransaction, List<T> ts) {
                            return true;
                        }
                    });
            InsertModelTransaction<T> transaction = new InsertModelTransaction<>(processModelInfo);
            TransactionManager.getInstance().addTransaction(transaction);
        }

        public void createUpdateTransaction(@Nullable final CountDownLatch countDownLatch)
                throws InstantiationException, IllegalAccessException {
            List<T> entries = new Select().from(mHandlerClass).queryList();
            for (T entry : entries) {
                entry.booleanValue = true;
            }

            ProcessModelInfo<T> processModelInfo = ProcessModelInfo.withModels(entries)
                    .result(new TransactionListener<List<T>>() {
                        @Override
                        public void onResultReceived(List<T> ts) {
                            if (countDownLatch != null) {
                                countDownLatch.countDown();
                            }
                        }

                        @Override
                        public boolean onReady(BaseTransaction<List<T>> baseTransaction) {
                            return true;
                        }

                        @Override
                        public boolean hasResult(BaseTransaction<List<T>> baseTransaction, List<T> ts) {
                            return true;
                        }
                    });
            UpdateModelListTransaction<T> transaction = new UpdateModelListTransaction<>(processModelInfo);
            TransactionManager.getInstance().addTransaction(transaction);
        }
    }
}
