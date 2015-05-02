package iiitd.ac.in.dsys.meetup.Services;

import android.app.IntentService;
import android.content.Intent;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DeactivateWakefulService extends IntentService {

    public DeactivateWakefulService() {
        super("DeactivateWakefulService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            //deactivate()
        }
    }

}
