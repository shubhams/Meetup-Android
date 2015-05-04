package iiitd.ac.in.dsys.meetup.GCM;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import iiitd.ac.in.dsys.meetup.Activities.MainActivity;
import iiitd.ac.in.dsys.meetup.Activities.MeetupActivity;
import iiitd.ac.in.dsys.meetup.R;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private static final String TAG = "GcmIntentService";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    Bundle notificationExtras;

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_FOO = "iiitd.ac.in.dsys.meetup.GCM.action.FOO";
    public static final String ACTION_BAZ = "iiitd.ac.in.dsys.meetup.GCM.action.BAZ";

    // TODO: Rename parameters
    public static final String EXTRA_PARAM1 = "iiitd.ac.in.dsys.meetup.GCM.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "iiitd.ac.in.dsys.meetup.GCM.extra.PARAM2";

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }

            Bundle extras = intent.getExtras();
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
            // The getMessageType() intent parameter must be the intent you received
            // in your BroadcastReceiver.
            String messageType = gcm.getMessageType(intent);

            if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
                if (GoogleCloudMessaging.
                        MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                    sendNotification("Send error: " + extras.toString(),"");
                } else if (GoogleCloudMessaging.
                        MESSAGE_TYPE_DELETED.equals(messageType)) {
                    sendNotification("Deleted messages on server: " +
                            extras.toString(),"");
                    // If it's a regular GCM message, do some work.
                } else if (GoogleCloudMessaging.
                        MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                    notificationExtras=extras;

                    Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                    // Post notification of received message.
                    if(extras.getString("collapse_key").equals("make_meetup")) {
                        sendNotification("Received: " + extras.toString(),"make_meetup");
                    }
                    else if(extras.getString("collapse_key").equals("meetup_accept")){
                        sendNotification("Received: " + extras.toString(),"meetup_accept");
                    }
                    else if(extras.getString("collapse_key").equals("meetup_deactivated")){
                        sendNotification("Received: " + extras.toString(),"meetup_deactivated");
                    }
                    else if(extras.getString("collapse_key").equals("meetup_activated")){
                        sendNotification("Received: " + extras.toString(),"meetup_activated");
                    }
                    else{
                        sendNotification("Received: " + extras.toString(),"");
                    }

                    Log.i(TAG, "Received: " + extras.toString()+" "+" ck="+extras.getString("collapse_key"));
                }
            }
            // Release the wake lock provided by the WakefulBroadcastReceiver.
            GcmBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg, String key) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent;

        if(key.equals("make_meetup")){
            Intent i = new Intent(this,MeetupActivity.class);
            i.putExtra("name",notificationExtras.getString("meetup_name"));
            i.putExtra("owner",notificationExtras.getString("meetup_owner_email"));
            i.putExtra("active",Boolean.getBoolean(notificationExtras.getString("active")));
            i.putExtra("accepted",false);

            contentIntent = PendingIntent.getActivity(this, 2204,i, PendingIntent.FLAG_UPDATE_CURRENT);
            msg=notificationExtras.getString("meetup_owner_email")+" invited you to join "
                    +notificationExtras.getString("meetup_name");
        }
        else if(key.equals("meetup_accepted")){
            Intent i = new Intent(this,MeetupActivity.class);
            i.putExtra("name",notificationExtras.getString("meetup_name"));
            i.putExtra("owner",notificationExtras.getString("meetup_owner_email"));
            i.putExtra("active",Boolean.getBoolean(notificationExtras.getString("active")));
            i.putExtra("accepted",false);

            contentIntent = PendingIntent.getActivity(this, 2205,i, PendingIntent.FLAG_UPDATE_CURRENT);
            msg=notificationExtras.getString("acceptor_email")+" joined "
                    +notificationExtras.getString("meetup_name");
        }
        else if(key.contains("meetup_deactivated")){
            Intent i = new Intent(this,MeetupActivity.class);
            i.putExtra("name",notificationExtras.getString("meetup_name"));
            i.putExtra("owner",notificationExtras.getString("meetup_owner_email"));
            i.putExtra("active",Boolean.getBoolean(notificationExtras.getString("active")));
            i.putExtra("accepted",false);

            contentIntent = PendingIntent.getActivity(this, 2206,i, PendingIntent.FLAG_UPDATE_CURRENT);
            msg=notificationExtras.getString("meetup_name")+" has been deactivated";
        }
        else if(key.equals("meetup_activated")){
            Intent i = new Intent(this,MeetupActivity.class);
            i.putExtra("name",notificationExtras.getString("meetup_name"));
            i.putExtra("owner",notificationExtras.getString("meetup_owner_email"));
            i.putExtra("active",Boolean.getBoolean(notificationExtras.getString("active")));
            i.putExtra("accepted",false);

            contentIntent = PendingIntent.getActivity(this, 2207,i, PendingIntent.FLAG_UPDATE_CURRENT);
            msg=notificationExtras.getString("meetup_name")+" has been activated";
        }
        else {
            contentIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, MainActivity.class), 0);
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Meetup")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
