package org.andmar1x.androidormtests.simple;

import android.support.annotation.Nullable;
import android.test.AndroidTestCase;

import org.andmar1x.androidormtests.Consts;
import org.andmar1x.androidormtests.realm.Entry1;
import org.andmar1x.androidormtests.realm.Entry2;
import org.andmar1x.androidormtests.realm.TransactionHelper;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by andmar1x on 5/20/15.
 */
public class RealmComplexTest extends AndroidTestCase {

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

    public void testInsertToOneTable() throws Throwable {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final EntryCreator<Entry1> entryCreator1 = new EntryCreator1(Entry1.class);
        new Thread() {
            @Override
            public void run() {
                entryCreator1.createInsertTransaction(countDownLatch);
            }
        }.start();

        entryCreator1.createInsertTransaction(countDownLatch);
        try {
            countDownLatch.await();
        } catch (Exception e) {
            fail(e.getMessage());
        }

        long count = mRealm.where(Entry1.class).count();

        assertEquals(Consts.ITEMS_COUNT * 2, count);
    }

    public void testInsertToTwoTables() throws Throwable {
        final CountDownLatch countDownLatch = new CountDownLatch(2);
        final EntryCreator<Entry1> entryCreator1 = new EntryCreator1(Entry1.class);
        new Thread() {
            @Override
            public void run() {
                entryCreator1.createInsertTransaction(countDownLatch);
            }
        }.start();

        EntryCreator<Entry2> entryCreator2 = new EntryCreator2(Entry2.class);
        entryCreator2.createInsertTransaction(countDownLatch);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }

        long count1 = mRealm.where(Entry1.class).count();
        long count2 = mRealm.where(Entry2.class).count();

        assertEquals(Consts.ITEMS_COUNT * 2, count1 + count2);
    }

    public void testInsertToOneTableAndUpdateAnother() throws Throwable {
        final CountDownLatch countDownLatch = new CountDownLatch(3);

        final EntryCreator<Entry2> entryCreator2 = new EntryCreator2(Entry2.class);
        entryCreator2.createInsertTransaction(countDownLatch);

        final EntryCreator<Entry1> entryCreator1 = new EntryCreator1(Entry1.class);
        new Thread() {
            @Override
            public void run() {
                entryCreator1.createInsertTransaction(countDownLatch);
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                entryCreator2.createUpdateTransaction(countDownLatch);
            }
        }.start();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }

        long countTrue = mRealm.where(Entry2.class).equalTo("booleanValue", true).count();

        assertEquals(Consts.ITEMS_COUNT, countTrue);

        long count1 = mRealm.where(Entry1.class).count();
        long count2 = mRealm.where(Entry2.class).count();

        assertEquals(Consts.ITEMS_COUNT * 2, count1 + count2);
    }

    private class EntryCreator1 extends EntryCreator<Entry1> {

        public EntryCreator1(Class<Entry1> handlerClass) {
            super(handlerClass);
        }

        @Override
        public Entry1 create(int val) {
            Entry1 entry = mRealm.createObject(Entry1.class);
            entry.setBooleanValue(val % 2 == 0);
            entry.setShortValue((short) val);
            entry.setIntValue(val);
//            entry.setLongValue(val);
            entry.setFloatValue(val);
            entry.setDoubleValue(val);
            entry.setStringValue(" " + val);
            entry.setDateValue(new Date());

            return entry;
        }

        @Override
        public void processEntries(List<Entry1> entries) {
            for (Entry1 entry : entries) {
                entry.setBooleanValue(true);
            }
        }
    }

    private class EntryCreator2 extends EntryCreator<Entry2> {

        public EntryCreator2(Class<Entry2> handlerClass) {
            super(handlerClass);
        }

        @Override
        public Entry2 create(int val) {
            Entry2 entry = mRealm.createObject(Entry2.class);
            entry.setBooleanValue(val % 2 == 0);
            entry.setShortValue((short) val);
            entry.setIntValue(val);
            entry.setLongValue(val);
            entry.setFloatValue(val);
            entry.setDoubleValue(val);
            entry.setStringValue(" " + val);
            entry.setDateValue(new Date());

            return entry;
        }

        @Override
        public void processEntries(List<Entry2> entries) {
            for (Entry2 entry : entries) {
                entry.setBooleanValue(true);
            }
        }
    }

    private abstract class EntryCreator<T extends RealmObject> {

        private final Class<T> mHandlerClass;

        public EntryCreator(final Class<T> handlerClass) {
            mHandlerClass = handlerClass;
        }

        public abstract T create(int val);

        public abstract void processEntries(List<T> entries);

        public void createInsertTransaction(@Nullable final CountDownLatch countDownLatch) {
            TransactionHelper.doInTransaction(mRealm, new TransactionHelper.Callback() {
                @Override
                public void onProcess() {
                    try {
                        for (int i = 1; i <= Consts.ITEMS_COUNT; ++i) {
                            create(i);
                        }
                    } catch (Exception e) {
                        fail(e.getMessage());
                    }
                }

                @Override
                public void onFail() {
                    fail("Transaction failed");
                }

                @Override
                public void onResultReceived() {
                    if (countDownLatch != null) {
                        countDownLatch.countDown();
                    }
                }
            });
        }

        public void createUpdateTransaction(@Nullable final CountDownLatch countDownLatch) {
            final List<T> entries = mRealm.where(mHandlerClass).findAll();
            TransactionHelper.doInTransaction(mRealm, new TransactionHelper.Callback() {
                @Override
                public void onProcess() {
                    processEntries(entries);
                }

                @Override
                public void onFail() {
                    fail("Transaction failed");
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
