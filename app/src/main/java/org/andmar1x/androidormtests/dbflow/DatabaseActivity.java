package org.andmar1x.androidormtests.dbflow;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.andmar1x.androidormtests.R;
import org.andmar1x.androidormtests.TestActivity;

import java.util.Date;
import java.util.List;

public class DatabaseActivity extends TestActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FlowManager.init(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        new Delete().from(Entry.class).query();
        FlowManager.destroy();
    }

    @Override
    public void onClick(View v) {
        int i = mAddEntryButton.hashCode();

        Entry entry = new Entry();
        entry.booleanValue = true;
        entry.shortValue = (short) i;
        entry.intValue = i;
        entry.floatValue = (float) i;
        entry.doubleValue = (double) i;
        entry.stringValue = String.valueOf(i);
        entry.dateValue = new Date();

        entry.insert();

        List<Entry> results = new Select().from(Entry.class).queryList();
        if (!results.isEmpty()) {
            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(results.get(0).stringValue);
        }
    }
}
