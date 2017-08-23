package com.friends.stay.keepintouch;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by amitropp on 19/08/2017.
 */

public class ManagerService extends IntentService {

    public ManagerService() {
        super("ManagerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Normally we would do some work here, like download a file.
        // For our sample, we just sleep for 5 seconds.
        try {
            Thread.sleep(1);
            while (true) {
                Log.d("here", "here");
            }
        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        }
    }
}
