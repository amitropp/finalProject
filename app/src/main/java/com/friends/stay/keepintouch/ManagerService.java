package com.friends.stay.keepintouch;

import android.app.IntentService;
import android.content.Intent;

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
            Thread.sleep(5000);
            while (true) {
                //TODO
            }
        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        }
    }
}
