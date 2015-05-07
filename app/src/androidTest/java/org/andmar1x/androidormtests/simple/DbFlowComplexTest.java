package org.andmar1x.androidormtests.simple;

import android.test.AndroidTestCase;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.runtime.transaction.BaseTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.TransactionListener;
import com.raizlabs.android.dbflow.runtime.transaction.process.InsertModelTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelInfo;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.andmar1x.androidormtests.Consts;
import org.andmar1x.androidormtests.dbflow.Entry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by andmar1x on 5/4/15.
 */
public class DbFlowComplexTest extends AndroidTestCase {

    public static final int THREAD_COUNT = 4;

    private ArrayList<Thread> mThreads = new ArrayList<>();

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        FlowManager.init(mContext);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        mThreads.clear();

        // Clear database
        new Delete()
                .from(Entry.class)
                .query();
        FlowManager.destroy();
    }

    public void testSingleInsertToOneTable() throws Throwable {
        CountDownLatch countDownLatch = new CountDownLatch(Consts.ITEMS_COUNT * THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; ++i) {
            SingleInsertThread thread = new SingleInsertThread(countDownLatch);
            mThreads.add(thread);
        }

        for (Thread thread : mThreads) {
            thread.start();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            fail();
        }

        long count = new Select()
                .count()
                .from(Entry.class)
                .count();

        assertEquals(Consts.ITEMS_COUNT * THREAD_COUNT, count);
    }

    public void testListInsertToOneTable() {
        CountDownLatch countDownLatch = new CountDownLatch(THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; ++i) {
            ListInsertThread thread = new ListInsertThread(countDownLatch);
            mThreads.add(thread);
        }

        for (Thread thread : mThreads) {
            thread.start();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            fail();
        }

        long count = new Select()
                .count()
                .from(Entry.class)
                .count();

        assertEquals(Consts.ITEMS_COUNT * THREAD_COUNT, count);
    }

    private Entry createEntry(int i) {
        Entry entry = new Entry();
        entry.booleanValue = (i % 2 == 0);
        entry.shortValue = (short) i;
        entry.intValue = i;
        entry.floatValue = (float) i;
        entry.doubleValue = (double) i;
        entry.stringValue = Entry.class.getSimpleName() + " " + i;
        entry.dateValue = new Date();

        return entry;
    }

    private void addSingleInsertTransaction(Entry entry, CountDownLatch countDownLatch) {
        ProcessModelInfo<Entry> processModelInfo = ProcessModelInfo.withModels(entry)
                .result(new TransactListener(countDownLatch));
        InsertModelTransaction<Entry> insertModelTransaction = new InsertModelTransaction<>(processModelInfo);
        TransactionManager.getInstance().addTransaction(insertModelTransaction);
    }

    private void addListInsertTransaction(List<Entry> entries, CountDownLatch countDownLatch) {
        ProcessModelInfo<Entry> processModelInfo = ProcessModelInfo.withModels(entries)
                .result(new TransactListener(countDownLatch));
        InsertModelTransaction<Entry> insertModelTransaction = new InsertModelTransaction<>(processModelInfo);
        TransactionManager.getInstance().addTransaction(insertModelTransaction);
    }

    private class TransactListener implements TransactionListener<List<Entry>> {

        private final CountDownLatch mCountDownLatch;

        public TransactListener(CountDownLatch countDownLatch) {
            mCountDownLatch = countDownLatch;
        }

        @Override
        public void onResultReceived(List<Entry> entries) {
            mCountDownLatch.countDown();
        }

        @Override
        public boolean onReady(BaseTransaction<List<Entry>> baseTransaction) {
            return true;
        }

        @Override
        public boolean hasResult(BaseTransaction<List<Entry>> baseTransaction, List<Entry> entries) {
            return true;
        }
    }

    private class TestableEntry1 extends Entry {

    }

    private class TestableEntry2 extends Entry {

    }

    private class SingleInsertThread extends Thread {

        private final CountDownLatch mCountDownLatch;

        public SingleInsertThread(CountDownLatch countDownLatch) {
            mCountDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            for (int i = 1; i <= Consts.ITEMS_COUNT; ++i) {
                addSingleInsertTransaction(createEntry(i), mCountDownLatch);
            }
        }
    }

    private class ListInsertThread<T> extends Thread {

        private final CountDownLatch mCountDownLatch;

        public ListInsertThread(CountDownLatch countDownLatch) {
            mCountDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            List<Entry> entries = new ArrayList<>();
            for (int i = 1; i <= Consts.ITEMS_COUNT; ++i) {
                entries.add(createEntry(i));
            }
            addListInsertTransaction(entries, mCountDownLatch);
        }
    }
}
