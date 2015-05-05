package org.andmar1x.androidormtests.ollie;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.andmar1x.androidormtests.R;
import org.andmar1x.androidormtests.TestActivity;

import java.util.Date;
import java.util.List;

import ollie.Ollie;
import ollie.query.Select;

public class DatabaseActivity extends TestActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Ollie.init(this, EntryDb.NAME, EntryDb.VERSION);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Delete.from(Entry.class).execute(); not works now. See https://github.com/pardom/Ollie/issues/23
        Ollie.getDatabase().delete(Entry.TABLE_NAME, "", new String[]{});
//        Ollie.getDatabase().close();
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

        List<Entry> results = Select.from(Entry.class).fetch();
        if (!results.isEmpty()) {
            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(results.get(0).stringValue);
        }
    }
}
