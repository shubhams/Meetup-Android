package iiitd.ac.in.dsys.meetup.messages;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.appspot.intense_terra_821.data_api.DataApi;
import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesMeetupDescMessage;
import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesUpMeetupMessageSmall;

import java.io.IOException;

import iiitd.ac.in.dsys.meetup.ObjectClasses.MeetupObject;
import iiitd.ac.in.dsys.meetup.TaskCompleteInterfaces.OnGetMeetupDetailsTaskCompleted;

/**
 * Created by vedantdasswain on 08/04/15.
 */
public class getMeetupDetailsTask extends AsyncTask<Void, Void, ApiCustomMessagesMeetupDescMessage> {
    Context context;
    DataApi dataApi;
    MeetupObject mo;
    private OnGetMeetupDetailsTaskCompleted listener;

    public final String TAG="getMeetupDetailsTask";

    public getMeetupDetailsTask(Context context, DataApi dataApi,MeetupObject mo, OnGetMeetupDetailsTaskCompleted listener) {
        this.context = context;
//        this.builder = builder;
        this.dataApi = dataApi;
        this.listener=listener;
        this.mo=mo;
    }

    @Override
    protected ApiCustomMessagesMeetupDescMessage doInBackground(Void... params) {
        ApiCustomMessagesMeetupDescMessage meetupDesc = new ApiCustomMessagesMeetupDescMessage();
        ApiCustomMessagesUpMeetupMessageSmall msgSmall = new ApiCustomMessagesUpMeetupMessageSmall();
        msgSmall.setName(mo.getName());
        msgSmall.setOwner(mo.getOwner());
        try {
            meetupDesc = dataApi.getMeetupDetails(msgSmall).execute();
            return meetupDesc;
        } catch (IOException e) {
            Log.d(TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return meetupDesc;
    }

    @Override
    protected void onPostExecute(ApiCustomMessagesMeetupDescMessage meetupDesc){
        Log.v(TAG, meetupDesc.toString());
        listener.onTaskCompleted(meetupDesc);
        return;
    }
}

