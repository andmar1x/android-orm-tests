package org.andmar1x.androidormtests;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    protected Button mAddEntryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        mAddEntryButton = (Button) findViewById(R.id.add_entry);
        mAddEntryButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
