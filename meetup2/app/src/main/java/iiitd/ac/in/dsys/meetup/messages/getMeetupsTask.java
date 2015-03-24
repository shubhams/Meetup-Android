package iiitd.ac.in.dsys.meetup.messages;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.appspot.intense_terra_821.data_api.DataApi;
import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesMeetupListMessage;
import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesMeetupMessage;

import java.io.IOException;

/**
 * Created by vedantdasswain on 25/03/15.
 */
public class getMeetupsTask extends AsyncTask<Void, Void, String> {
    Context context;
    DataApi dataApi;

    public final String TAG="getMeetupsTask";

    @Override
    protected String doInBackground(Void... params) {
        try{
            ApiCustomMessagesMeetupListMessage meetupsList=dataApi.getMeetups().execute();
            if(meetupsList!=null) {
                Log.v(TAG,"Success message: "+meetupsList.getSuccess().getStrValue());
                if (!meetupsList.isEmpty() && meetupsList.getMeetups() != null)
                    for (ApiCustomMessagesMeetupMessage meetup : meetupsList.getMeetups())
                        Log.v(TAG, "Meetup name: " + meetup.getName());
                else
                    Log.v(TAG, "No meetups");
            }
            return "Retrieved meetups";
        } catch (IOException e) {
            Log.d(TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    return "Failed to get meetups";
    }

    protected void onPostExecute(String message){
        Log.v(TAG, message);
        return ;
    }
}
