package org.andmar1x.androidormtests;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.raizlabs.android.dbflow.config.FlowManager;

import org.andmar1x.androidormtests.dbflow.Entry;
import org.andmar1x.androidormtests.dbflow.EntryDb;

import java.util.Date;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        FlowManager.init(this);
        for (int i = 1; i <= Consts.ITEMS_COUNT; ++i) {
            insertOne(i);
        }
    }

    @Override
    protected void onDestroy() {
        FlowManager.destroy();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        FlowManager.getDatabase(EntryDb.NAME).getWritableDatabase().close();
        FlowManager.getDatabase(EntryDb.NAME).reset(this);
        for (int i = 1; i <= Consts.ITEMS_COUNT; ++i) {
            insertOne(i);
        }
    }

    private void insertOne(int i) {
        Entry entry = new Entry();
        entry.booleanValue = (i % 2 == 0);
        entry.shortValue = (short) i;
        entry.intValue = i;
        entry.floatValue = (float) i;
        entry.doubleValue = (double) i;
        entry.stringValue = Entry.class.getSimpleName() + " " + i;
        entry.dateValue = new Date();

        entry.insert();
    }
}
