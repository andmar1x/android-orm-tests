package org.andmar1x.androidormtests.dbflow;

import com.raizlabs.android.dbflow.annotation.Database;

import org.andmar1x.androidormtests.BuildConfig;

/**
 * Created by andmar1x on 5/4/15.
 */
@Database(name = EntryDb.NAME, version = EntryDb.VERSION, foreignKeysSupported = true)
public class EntryDb {

    public static final String NAME = "dbflow_test";

    public static final int VERSION = BuildConfig.VERSION_CODE;
}
