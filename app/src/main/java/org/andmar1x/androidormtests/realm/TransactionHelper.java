package org.andmar1x.androidormtests.realm;

import android.content.Context;

import io.realm.Realm;

/**
 * Created by andmar1x on 5/20/15.
 */
public class TransactionHelper {

    public static void doInTransaction(Realm realm, Callback callback) {
//        synchronized (Realm.class) {
        realm.beginTransaction();
            try {
                callback.onProcess();
                realm.commitTransaction();
            } catch (Exception e) {
                realm.cancelTransaction();
                callback.onFail();
            }
            callback.onResultReceived();
//        }
    }

    public interface Callback {

        void onProcess();

        void onFail();

        void onResultReceived();
    }
}
