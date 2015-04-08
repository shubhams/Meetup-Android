package iiitd.ac.in.dsys.meetup.messages;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.appspot.intense_terra_821.data_api.DataApi;
import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesMeetupListMessage;

import java.io.IOException;

import iiitd.ac.in.dsys.meetup.TaskCompleteInterfaces.OnGetMeetupsTaskCompleted;

/**
 * Created by vedantdasswain on 08/04/15.
 */
public class getUnacceptedMeetupsTask extends AsyncTask<Void, Void, ApiCustomMessagesMeetupListMessage> {
    Context context;
    DataApi dataApi;
    private OnGetMeetupsTaskCompleted listener;

    public final String TAG="getUnMeetupsTask";

    public getUnacceptedMeetupsTask(Context context, DataApi dataApi, OnGetMeetupsTaskCompleted listener) {
        this.context = context;
//        this.builder = builder;
        this.dataApi = dataApi;
        this.listener=listener;
    }

    @Override
    protected ApiCustomMessagesMeetupListMessage doInBackground(Void... params) {
        ApiCustomMessagesMeetupListMessage meetupsList = new ApiCustomMessagesMeetupListMessage();
        try {
            meetupsList = dataApi.getMeetupsUnaccepted().execute();
            return meetupsList;
        } catch (IOException e) {
            Log.d(TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return meetupsList;
    }

    @Override
    protected void onPostExecute(ApiCustomMessagesMeetupListMessage meetupsList){
        Log.v(TAG, meetupsList.toString());
        listener.onTaskCompleted(meetupsList,false);
        return;
    }
}

