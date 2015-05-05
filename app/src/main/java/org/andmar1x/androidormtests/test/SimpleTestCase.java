package org.andmar1x.androidormtests.test;

import android.test.AndroidTestCase;

/**
 * Created by andmar1x on 5/2/15.
 */
public abstract class SimpleTestCase extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public abstract void testSingleInsert();

    public abstract void testBulkInsert();
}
