package org.andmar1x.androidormtests.realm;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.andmar1x.androidormtests.R;
import org.andmar1x.androidormtests.TestActivity;

import java.util.Date;
import java.util.List;

import io.realm.Realm;

public class DatabaseActivity extends TestActivity {

    protected Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Realm.deleteRealmFile(this);
        mRealm = Realm.getInstance(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mRealm != null) {
            mRealm.close();
        }
    }

    @Override
    public void onClick(View v) {
        int i = mAddEntryButton.hashCode();

        mRealm.beginTransaction();

        Entry entry = mRealm.createObject(Entry.class);
        entry.setBooleanValue(i % 2 == 0);
        entry.setShortValue((short) i);
        entry.setIntValue(i);
        entry.setLongValue(i);
        entry.setFloatValue(i);
        entry.setDoubleValue(i);
        entry.setStringValue(String.valueOf(i));
        entry.setDateValue(new Date());

        mRealm.commitTransaction();

        List<Entry> results = mRealm.where(Entry.class).findAll();
        if (!results.isEmpty()) {
            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(results.get(0).getStringValue());
        }
    }
}
