package iiitd.ac.in.dsys.meetup.ObjectClasses;

import android.app.PendingIntent;

/**
 * Created by vedantdasswain on 04/05/15.
 */
public class MeetupAlarmIntent {
    String meetupName;
    PendingIntent alarmIntent;

    public MeetupAlarmIntent(String name,PendingIntent intent){
        meetupName=name;
        alarmIntent=intent;
    }

    public String getMeetupName(){
        return meetupName;
    }
    public PendingIntent getAlarmIntent(){
        return alarmIntent;
    }
}
