package iiitd.ac.in.dsys.meetup.TaskCompleteInterfaces;

import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesMeetupLocationsUpdateFullMessage;

/**
 * Created by Shubham on 10 Apr 15.
 */
public interface OnHeartBeatCompleted {
    void onHeatBeatReceived(ApiCustomMessagesMeetupLocationsUpdateFullMessage fullMessage);
}
