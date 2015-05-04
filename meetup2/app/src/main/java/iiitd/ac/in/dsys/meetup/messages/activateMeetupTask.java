package iiitd.ac.in.dsys.meetup.messages;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.appspot.intense_terra_821.data_api.DataApi;
import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesSuccessMessage;
import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesUpMeetupMessageOwner;
import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesUpMeetupMessageSmall;

import java.io.IOException;

import iiitd.ac.in.dsys.meetup.ObjectClasses.MeetupObject;
import iiitd.ac.in.dsys.meetup.TaskCompleteInterfaces.OnActivateMeetupTaskCompleted;

/**
 * Created by vedantdasswain on 02/05/15.
 */
public class activateMeetupTask extends AsyncTask<Void, Void, ApiCustomMessagesSuccessMessage> {
    Context context;
    DataApi dataApi;
    MeetupObject mo;
    private OnActivateMeetupTaskCompleted listener;

    public final String TAG="activateMeetupTask";

    public activateMeetupTask(Context context, DataApi dataApi, MeetupObject mo, OnActivateMeetupTaskCompleted listener) {
        this.context = context;
//        this.builder = builder;
        this.dataApi = dataApi;
        this.listener=listener;
        this.mo=mo;
    }

    @Override
    protected ApiCustomMessagesSuccessMessage doInBackground(Void... params) {
        ApiCustomMessagesSuccessMessage successMessage = new ApiCustomMessagesSuccessMessage();
        ApiCustomMessagesUpMeetupMessageSmall msgSmall = new ApiCustomMessagesUpMeetupMessageSmall();
        msgSmall.setName(mo.getName());
        msgSmall.setOwner(mo.getOwner());
        ApiCustomMessagesUpMeetupMessageOwner msgUpMeetupMsgOwner=new ApiCustomMessagesUpMeetupMessageOwner();
        msgUpMeetupMsgOwner.setMeetupName(mo.getName());
        try {
            successMessage = dataApi.activateMeetup(msgUpMeetupMsgOwner).execute();
            return successMessage;
        }
        catch (IOException e) {
            Log.d(TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return successMessage;
    }

    @Override
    protected void onPostExecute(ApiCustomMessagesSuccessMessage meetupSuccess){
        Log.v(TAG, meetupSuccess.toString());
        listener.onActivateTaskCompleted(meetupSuccess);
        return;
    }
}
