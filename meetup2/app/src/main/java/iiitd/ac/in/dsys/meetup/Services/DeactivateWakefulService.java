package iiitd.ac.in.dsys.meetup.Services;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.appspot.intense_terra_821.data_api.DataApi;
import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesSuccessMessage;

import iiitd.ac.in.dsys.meetup.CommonUtils;
import iiitd.ac.in.dsys.meetup.ObjectClasses.MeetupObject;
import iiitd.ac.in.dsys.meetup.R;
import iiitd.ac.in.dsys.meetup.TaskCompleteInterfaces.OnDeactivateMeetupTaskCompleted;
import iiitd.ac.in.dsys.meetup.messages.deactivateMeetupTask;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DeactivateWakefulService extends IntentService implements OnDeactivateMeetupTaskCompleted{

    DataApi dataApiInst;
    MeetupObject mo;

    public DeactivateWakefulService() {
        super("DeactivateWakefulService");
        dataApiInst= CommonUtils.getDataApiInst();

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            //deactivate()
            Bundle extras = intent.getExtras();
            if(extras !=null) {
                mo = new MeetupObject(extras.getString("name"), extras.getString("owner"),
                        extras.getBoolean("active"), extras.getBoolean("accepted"));
            }
            (new deactivateMeetupTask(this,dataApiInst,mo,this)).execute();

        }
    }

    @Override
    public void onDeactivateTaskCompleted(ApiCustomMessagesSuccessMessage meetupSuccess) {
        if (meetupSuccess.getStrValue().contains("deactivated")) {
            AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            PendingIntent alarmIntent;
            mo.setActive(false);
            if (alarmMgr != null) {
                alarmIntent = CommonUtils.getAlarmIntentByName(mo.getName());
                alarmMgr.cancel(alarmIntent);
                Toast.makeText(this, "Deactivated", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            mo.setActive(true);
            // 1. Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage(meetupSuccess.getStrValue())
                    .setTitle("Dayyuuum!");

            // 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();

            dialog.show();
        }
    }

    private void notifyDeactivate(String meetupName){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Meetup Over")
                        .setContentText(meetupName+" has been deactivated");

        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}
