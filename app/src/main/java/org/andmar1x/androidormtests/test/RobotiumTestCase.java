package org.andmar1x.androidormtests.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import com.robotium.solo.Solo;

import org.andmar1x.androidormtests.TestActivity;

/**
 * Created by andmar1x on 5/5/15.
 */
public class RobotiumTestCase<T extends TestActivity> extends ActivityInstrumentationTestCase2<T> {

    private Solo mSolo;

    public RobotiumTestCase(Class<T> activityClass) {
        super(activityClass);
    }

    @Override
    public void setUp() throws Exception {
        mSolo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        mSolo.finishOpenedActivities();
    }

    public void testAddEntry() throws Exception {
        //Unlock the lock screen
        mSolo.unlockScreen();
        //Assert that DatabaseActivity activity is opened
        mSolo.assertCurrentActivity("Expected DatabaseActivity", "DatabaseActivity");

        mSolo.clickOnButton("Add Entry");

        mSolo.takeScreenshot();

        Button button = mSolo.getButton("Add Entry");
        assertTrue("Entry is not inserted", mSolo.searchText(String.valueOf(button.hashCode())));
    }
}
