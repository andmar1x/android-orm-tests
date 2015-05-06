package org.andmar1x.androidormtests.activeandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import org.andmar1x.androidormtests.R;
import org.andmar1x.androidormtests.TestActivity;

import java.util.Date;
import java.util.List;

public class DatabaseActivity extends TestActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActiveAndroid.initialize(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        new Delete().from(Entry.class).execute();
    }

    @Override
    public void onClick(View v) {
        int i = mAddEntryButton.hashCode();

        Entry entry = new Entry();
        entry.booleanValue = (i % 2 == 0);
        entry.shortValue = (short) i;
        entry.intValue = i;
        entry.longValue = (long) i;
        entry.floatValue = (float) i;
        entry.doubleValue = (double) i;
        entry.stringValue = String.valueOf(i);
        entry.dateValue = new Date();

        entry.save();

        List<Entry> results = new Select().from(Entry.class).execute();
        if (!results.isEmpty()) {
            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(results.get(0).stringValue);
        }
    }
}
