package iiitd.ac.in.dsys.meetup.messages;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.appspot.intense_terra_821.data_api.DataApi;
import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesMeetupListMessage;

import java.io.IOException;

import iiitd.ac.in.dsys.meetup.TaskCompleteInterfaces.OnGetMeetupsTaskCompleted;

/**
 * Created by vedantdasswain on 25/03/15.
 */
public class getMeetupsTask extends AsyncTask<Void, Void, String> {
    Context context;
    DataApi dataApi;
    ApiCustomMessagesMeetupListMessage meetupsList;
    private OnGetMeetupsTaskCompleted listener;

    public final String TAG="getMeetupsTask";

    public getMeetupsTask(Context context, DataApi dataApi,OnGetMeetupsTaskCompleted listener) {
        this.context = context;
//        this.builder = builder;
        this.dataApi = dataApi;
        this.listener=listener;
    }

    @Override
    protected String doInBackground(Void... params) {
        try{
            meetupsList=dataApi.getMeetups().execute();
            return "Retrieved meetups";
        } catch (IOException e) {
            Log.d(TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    return "Failed to get meetups";
    }

    @Override
    protected void onPostExecute(String message){
        Log.v(TAG, message);
        listener.onTaskCompleted(meetupsList);
        return ;
    }
}
