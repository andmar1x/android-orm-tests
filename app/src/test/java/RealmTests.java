import android.content.Context;

import junit.framework.Assert;

import org.andmar1x.androidormtests.realm.Entry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Date;

import io.realm.Realm;

/**
 * Created by andmar1x on 4/29/15.
 */
@PrepareForTest({Context.class, Realm.class, Entry.class})
@RunWith(PowerMockRunner.class)
public class RealmTests {

    protected Context mContext;

    protected Realm mRealm;

    @Before
    public void before() throws Exception {
        mContext = Mockito.mock(Context.class);

//        File fileMock = Mockito.mock(File.class);
//        Mockito.when(fileMock.getAbsolutePath()).thenReturn("/my/fake/file/path");
//        Mockito.when(fileMock.isDirectory()).thenReturn(true);
//
//        PowerMockito.whenNew(File.class).withParameterTypes(String.class).withArguments(Mockito.anyString()).thenReturn(fileMock);
//        PowerMockito.whenNew(File.class).withParameterTypes(File.class, String.class).withArguments(Mockito.any(File.class), Mockito.anyString()).thenReturn(fileMock);
//
//        Mockito.when(mContext.getFilesDir()).thenReturn(fileMock);

//        mRealm = Realm.getInstance(mContext);
        PowerMockito.mockStatic(Realm.class);
        mRealm = Mockito.mock(Realm.class);
        Mockito.when(Realm.getInstance(mContext)).thenReturn(mRealm);
    }

    @After
    public void after() {
        if (mRealm != null) {
            mRealm.close();
        }
    }

    @Test
    public void testCreateObject() {
        mRealm.beginTransaction();

        Entry entry = new Entry();
        entry.setDateValue(new Date());
        Mockito.when(mRealm.createObject(Entry.class)).thenReturn(entry);

        Entry newEntry = mRealm.createObject(Entry.class);

        mRealm.commitTransaction();

        Assert.assertEquals(entry, newEntry);

        Mockito.verify(mRealm).beginTransaction();
        Mockito.verify(mRealm).createObject(Entry.class);
        Mockito.verify(mRealm).commitTransaction();
    }
}
