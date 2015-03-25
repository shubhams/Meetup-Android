package iiitd.ac.in.dsys.meetup.TaskCompleteInterfaces;

import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesMeetupListMessage;

/**
 * Created by vedantdasswain on 25/03/15.
 */
public interface OnGetMeetupsTaskCompleted {
    void onTaskCompleted(ApiCustomMessagesMeetupListMessage meetupsList);
}
