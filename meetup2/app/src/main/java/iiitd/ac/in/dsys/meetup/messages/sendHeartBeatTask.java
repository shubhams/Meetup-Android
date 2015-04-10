package iiitd.ac.in.dsys.meetup.messages;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.appspot.intense_terra_821.data_api.DataApi;
import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesMeetupLocationsUpdateFullMessage;
import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesUpLocationMessage;
import iiitd.ac.in.dsys.meetup.TaskCompleteInterfaces.OnHeartBeatCompleted;

import java.io.IOException;

/**
 * Created by Shubham on 10 Apr 15.
 */
public class sendHeartBeatTask extends AsyncTask<Void,Void,ApiCustomMessagesMeetupLocationsUpdateFullMessage> {

    private static String TAG;
    private Context context;
    private DataApi dataApi;
    private OnHeartBeatCompleted listener;
    private ApiCustomMessagesUpLocationMessage upLocationMessage;

    public sendHeartBeatTask(Context context, DataApi dataApi,
                             ApiCustomMessagesUpLocationMessage receivedMessage, OnHeartBeatCompleted listener)
    {
        TAG = this.getClass().getSimpleName();
        this.context = context;
        this.dataApi = dataApi;
        this.upLocationMessage = receivedMessage;
        this.listener = listener;
        Log.d(TAG, String.valueOf(upLocationMessage));
    }

    @Override
    protected void onPostExecute(ApiCustomMessagesMeetupLocationsUpdateFullMessage apiCustomMessagesMeetupLocationsUpdateFullMessage) {
        super.onPostExecute(apiCustomMessagesMeetupLocationsUpdateFullMessage);
        if (apiCustomMessagesMeetupLocationsUpdateFullMessage!=null)
        {
            listener.onHeatBeatReceived(apiCustomMessagesMeetupLocationsUpdateFullMessage);
        }
        else
            Log.d(TAG,"response object is null");
    }

    @Override
    protected ApiCustomMessagesMeetupLocationsUpdateFullMessage doInBackground(Void... params) {
        try {
            return dataApi.heartbeat(upLocationMessage).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
