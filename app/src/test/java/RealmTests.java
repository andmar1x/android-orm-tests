import android.content.Context;

import com.google.gson.reflect.TypeToken;

import junit.framework.Assert;

import org.andmar1x.androidormtests.Consts;
import org.andmar1x.androidormtests.realm.Entry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by andmar1x on 4/29/15.
 */
@PrepareForTest({ Context.class, Realm.class, Entry.class })
@RunWith(PowerMockRunner.class)
public class RealmTests {

    protected Context mContext;

    protected Realm mRealm;

    @Before
    public void before() {
        mContext = PowerMockito.mock(Context.class);

        Realm.deleteRealmFile(mContext);
        PowerMockito.suppress(PowerMockito.constructor(Realm.class));
        PowerMockito.mockStatic(Realm.class);

        mRealm = PowerMockito.mock(Realm.class);//Realm.getInstance(mContext);
    }

    @After
    public void after() {
        if (mRealm != null) {
            mRealm.close();
        }
    }

    @Test
    public void testSingleInsert() {
        insert(false);

        mRealm.beginTransaction();

        Entry entry = PowerMockito.mock(Entry.class);
        RealmResults<Entry> query = PowerMockito.mock(RealmResults.class);
        PowerMockito.when(mRealm.where(Entry.class).findAll()).thenReturn(query);

        mRealm.cancelTransaction();

        Assert.assertEquals(query.size(), Consts.ITEMS_COUNT);
    }

    @Test
    public void testBulkInsert() {
        insert(true);
//        RealmResults<Entry> query = mRealm.where(Entry.class).findAll();

//        Assert.assertEquals(query.size(), Consts.ITEMS_COUNT);
    }

    private void insert(boolean isBulk) {
        if (isBulk) {
            mRealm.beginTransaction();
        }

        for (int i = 1; i <= Consts.ITEMS_COUNT; ++i) {
            if (!isBulk) {
                mRealm.beginTransaction();
            }

            Entry entry = PowerMockito.mock(Entry.class);
            PowerMockito.when(mRealm.createObject(Entry.class)).thenReturn(entry);
            entry.setBooleanValue(i % 2 == 0);
            entry.setShortValue((short) i);
            entry.setIntValue(i);
            entry.setLongValue(i);
            entry.setFloatValue(i);
            entry.setDoubleValue(i);
            entry.setStringValue(Entry.class.getSimpleName() + " " + i);
            entry.setDateValue(new Date());

            if (!isBulk) {
                mRealm.cancelTransaction();
            }
        }

        if (isBulk) {
            mRealm.cancelTransaction();
        }
    }

    private void select() {
        mRealm.beginTransaction();

        RealmResults<Entry> query = PowerMockito.mock(RealmResults.class);
        PowerMockito.when(mRealm.where(Entry.class).findAll()).thenReturn(query);

        mRealm.cancelTransaction();
    }
}
