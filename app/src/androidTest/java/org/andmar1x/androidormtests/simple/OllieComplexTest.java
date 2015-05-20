package org.andmar1x.androidormtests.simple;

import android.test.AndroidTestCase;

import org.andmar1x.androidormtests.Consts;
import org.andmar1x.androidormtests.ollie.Entry;
import org.andmar1x.androidormtests.ollie.Entry1;
import org.andmar1x.androidormtests.ollie.Entry2;
import org.andmar1x.androidormtests.ollie.EntryDb;
import org.andmar1x.androidormtests.ollie.TransactionHelper;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import ollie.Ollie;
import ollie.query.Select;

/**
 * Created by andmar1x on 5/4/15.
 */
public class OllieComplexTest extends AndroidTestCase {

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
        Ollie.getDatabase().delete(Entry1.TABLE_NAME, "", new String[]{});
        Ollie.getDatabase().delete(Entry2.TABLE_NAME, "", new String[]{});
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

        entryCreator.createInsertTransaction(countDownLatch);
        try {
            countDownLatch.await();
        } catch (Exception e) {
            fail();
        }

        long count = Select.columns("COUNT(*)").from(Entry.class).fetchValue(Integer.class);

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
        entryCreator2.createInsertTransaction(countDownLatch);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            fail();
        }

        long count1 = Select.columns("COUNT(*)").from(Entry1.class).fetchValue(Integer.class);
        long count2 = Select.columns("COUNT(*)").from(Entry2.class).fetchValue(Integer.class);

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

        long countTrue = Select.columns("COUNT(*)").from(Entry2.class).where("booleanValue=1")
                .fetchValue(Integer.class);

        assertEquals(Consts.ITEMS_COUNT, countTrue);

        long count1 = Select.columns("COUNT(*)").from(Entry1.class).fetchValue(Integer.class);
        long count2 = Select.columns("COUNT(*)").from(Entry2.class).fetchValue(Integer.class);

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

        public void createInsertTransaction(final CountDownLatch countDownLatch) {
            TransactionHelper.doInTransaction(new TransactionHelper.Callback() {
                @Override
                public void onProcess() {
                    for (int i = 1; i <= Consts.ITEMS_COUNT; ++i) {
                        T entry = null;
                        try {
                            entry = create(i);
                        } catch (Exception e) {
                            fail();
                        }
                        entry.save();
                    }
                }

                @Override
                public void onFail() {
                    fail();
                }

                @Override
                public void onResultReceived() {
                    if (countDownLatch != null) {
                        countDownLatch.countDown();
                    }
                }
            });
        }

        public void createUpdateTransaction(final CountDownLatch countDownLatch) {
            final List<T> entries = Select.from(mHandlerClass).fetch();

            TransactionHelper.doInTransaction(new TransactionHelper.Callback() {
                @Override
                public void onProcess() {
                    for (T entry : entries) {
                        entry.booleanValue = true;
                        entry.save();
                    }
                }

                @Override
                public void onFail() {
                    fail();
                }

                @Override
                public void onResultReceived() {
                    if (countDownLatch != null) {
                        countDownLatch.countDown();
                    }
                }
            });
        }
    }
}
