package iiitd.ac.in.dsys.meetup.Receivers;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import iiitd.ac.in.dsys.meetup.Services.DeactivateWakefulService;

/**
 * Created by vedantdasswain on 02/05/15.
 */
public class DeactivateAlarmReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // This is the Intent to deliver to our service.
        Intent service = new Intent(context, DeactivateWakefulService.class);

        // Start the service, keeping the device awake while it is launching.
        Log.i("DeactivateWakefulRcvr", "Starting service @ " + SystemClock.elapsedRealtime());
        startWakefulService(context, service);
    }
}
