package iiitd.ac.in.dsys.meetup;

import android.app.PendingIntent;

import com.appspot.intense_terra_821.data_api.DataApi;
import com.appspot.intense_terra_821.users_api.UsersApi;

import java.util.ArrayList;

import iiitd.ac.in.dsys.meetup.ObjectClasses.MeetupAlarmIntent;

/**
 * Created by vedantdasswain on 25/03/15.
 */
public class CommonUtils {
    static UsersApi usersApiInst;
    static DataApi dataApiInst;
    static PendingIntent alarmIntent;
    static ArrayList<MeetupAlarmIntent> alarmIntents = new ArrayList<MeetupAlarmIntent>();

    public static UsersApi getUsersApiInst(){
        return usersApiInst;
    }

    public static DataApi getDataApiInst(){
        return dataApiInst;
    }

    public static PendingIntent getAlarmIntent() { return alarmIntent; }

    public static PendingIntent getAlarmIntentByName(String name){
        for(MeetupAlarmIntent mai: alarmIntents){
            if(name.equals(mai.getMeetupName()))
                return mai.getAlarmIntent();
        }
        return null;
    }

    public static int getNextIndexOfAlarmIntents(){
        if(alarmIntents==null)
            return 0;
        else
            return alarmIntents.size();
    }

    public static void setUsersApiInst(UsersApi musersApiInst){
        usersApiInst=musersApiInst;
    }

    public static void setDataApiInst(DataApi mdataApiInst){
        dataApiInst=mdataApiInst;
    }

    public static void setAlarmIntent(PendingIntent malarmIntent) {alarmIntent=malarmIntent;}

    public static void setAlarmIntentToList(MeetupAlarmIntent mai){
        alarmIntents.add(mai);
    }
}
