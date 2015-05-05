package org.andmar1x.androidormtests.ormlite;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;

import org.andmar1x.androidormtests.R;
import org.andmar1x.androidormtests.TestActivity;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class DatabaseActivity extends TestActivity {

    private EntryDbHelper mEntryDbHelper;
    private Dao<Entry, Long> mEntryDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mEntryDbHelper = new EntryDbHelper(this);
            mEntryDao = mEntryDbHelper.getEntryDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            mEntryDbHelper.clearTable();
            mEntryDbHelper.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        try {
            int i = mAddEntryButton.hashCode();

            Entry entry = new Entry();
            entry.booleanValue = (i % 2 == 0);
            entry.shortValue = (short) i;
            entry.intValue = i;
            entry.floatValue = (float) i;
            entry.doubleValue = (double) i;
            entry.stringValue = String.valueOf(i);
            entry.dateValue = new Date();

            mEntryDao.create(entry);

            List<Entry> results = mEntryDao.query(mEntryDao.queryBuilder().where().prepare());
            if (!results.isEmpty()) {
                TextView textView = (TextView) findViewById(R.id.textView);
                textView.setText(results.get(0).stringValue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
