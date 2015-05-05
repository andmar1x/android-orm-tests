package org.andmar1x.androidormtests.greendao;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.andmar1x.androidormtests.R;
import org.andmar1x.androidormtests.TestActivity;

import java.util.Date;
import java.util.List;

public class DatabaseActivity extends TestActivity {

    private DaoSession mDaoSession;
    private EntryDao mEntryDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SQLiteDatabase db = new DaoMaster.DevOpenHelper(this, EntryDb.NAME, null).getWritableDatabase();
        mDaoSession = new DaoMaster(db).newSession();
        mEntryDao = mDaoSession.getEntryDao();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mEntryDao.deleteAll();
        mDaoSession.getDatabase().close();
    }

    @Override
    public void onClick(View v) {
        int i = mAddEntryButton.hashCode();

        Entry entry = new Entry();
        entry.setBooleanValue(i % 2 == 0);
        entry.setShortValue((short) i);
        entry.setIntValue(i);
        entry.setLongValue((long) i);
        entry.setFloatValue((float) i);
        entry.setDoubleValue((double) i);
        entry.setStringValue(String.valueOf(i));
        entry.setDateValue(new Date());

        mEntryDao.insert(entry);

        List<Entry> results = mEntryDao.queryBuilder().list();
        if (!results.isEmpty()) {
            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(results.get(0).getStringValue());
        }
    }
}
