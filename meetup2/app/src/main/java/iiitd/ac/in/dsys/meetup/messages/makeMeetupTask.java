package iiitd.ac.in.dsys.meetup.messages;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.appspot.intense_terra_821.data_api.DataApi;
import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesSuccessMessage;
import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesUpMeetupCreateMessage;

import java.io.IOException;

import iiitd.ac.in.dsys.meetup.TaskCompleteInterfaces.OnMakeMeetupTaskCompleted;

/**
 * Created by vedantdasswain on 25/03/15.
 */
public class makeMeetupTask extends AsyncTask<Void, Void, String> {
    private static final String TAG ="makeMeetupTask" ;
    Context context;
    DataApi dataApi;
    OnMakeMeetupTaskCompleted listener;
    ApiCustomMessagesUpMeetupCreateMessage createMessage;

    public makeMeetupTask(Context context, DataApi dataApi,OnMakeMeetupTaskCompleted listener
                ,ApiCustomMessagesUpMeetupCreateMessage createMessage) {
        this.context = context;
//        this.builder = builder;
        this.dataApi = dataApi;
        this.listener=listener;
        this.createMessage=createMessage;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            ApiCustomMessagesSuccessMessage msg=dataApi.makeMeetup(createMessage).execute();
            return msg.getStrValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Failed to create meetup";
    }

    @Override
    protected void onPostExecute(String message){
        Log.v(TAG, message);
        listener.onTaskCompleted(message);
        return ;
    }
}
