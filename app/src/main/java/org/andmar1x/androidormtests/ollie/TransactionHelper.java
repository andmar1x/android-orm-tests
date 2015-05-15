package org.andmar1x.androidormtests.ollie;

import android.database.sqlite.SQLiteDatabase;

import ollie.Ollie;

/**
 * Created by andmar1x on 5/15/15.
 */
public class TransactionHelper {

    public static void doInTransaction(Callback callback) {
        synchronized (Ollie.class) {
            SQLiteDatabase db = Ollie.getDatabase();
            db.beginTransaction();
            try {
                callback.onProcess();
                db.setTransactionSuccessful();
            } catch (Exception e) {
                callback.onFail();
            } finally {
                db.endTransaction();
            }
            callback.onResultReceived();
        }
    }

    public interface Callback {
        void onProcess();

        void onFail();

        void onResultReceived();
    }
}
