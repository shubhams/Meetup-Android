package iiitd.ac.in.dsys.meetup.DrawerSectionFragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.appspot.intense_terra_821.data_api.DataApi;
import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesMeetupListMessage;
import com.appspot.intense_terra_821.data_api.model.ApiCustomMessagesMeetupMessage;

import java.util.ArrayList;

import iiitd.ac.in.dsys.meetup.Activities.MeetupActivity;
import iiitd.ac.in.dsys.meetup.CommonUtils;
import iiitd.ac.in.dsys.meetup.CustomUI.MeetupListAdapter;
import iiitd.ac.in.dsys.meetup.ObjectClasses.MeetupObject;
import iiitd.ac.in.dsys.meetup.TaskCompleteInterfaces.OnGetMeetupsTaskCompleted;
import iiitd.ac.in.dsys.meetup.messages.getMeetupsTask;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class MeetupListFragment extends ListFragment implements OnGetMeetupsTaskCompleted {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private MeetupListAdapter adapter;
    private ArrayList<MeetupObject> meetupObjectsList;
    private static final String TAG="MeetupListFragment";

    // TODO: Rename and change types of parameters
    public static MeetupListFragment newInstance(String param1, String param2) {
        MeetupListFragment fragment = new MeetupListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    DataApi dataApiInst;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MeetupListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        meetupObjectsList=new ArrayList<MeetupObject>();
    }

    @Override
    public void onResume(){
        super.onResume();
//        Log.v(TAG,"onResume");
        getMeetups();
    }

    private void getMeetups() {
        Log.v(TAG,"Start fetching meetups...");
        dataApiInst= CommonUtils.getDataApiInst();
        (new getMeetupsTask(getActivity(), dataApiInst,this)).execute();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(getActivity(),MeetupActivity.class);
        i.putExtra("name",meetupObjectsList.get(position).getName());
        i.putExtra("owner",meetupObjectsList.get(position).getOwner());
        i.putExtra("active",meetupObjectsList.get(position).getActive());
        i.putExtra("accepted",meetupObjectsList.get(position).getAccepted());


        startActivity(i);
    }

    @Override
    public void onTaskCompleted(ApiCustomMessagesMeetupListMessage meetupsList,Boolean accepted) {
        if(meetupsList!=null) {
            if(meetupsList.getSuccess()!=null)
                Log.v(TAG, "Success message: " + meetupsList.getSuccess().getStrValue());
            if (!meetupsList.isEmpty() && meetupsList.getMeetups() != null) {
                for (ApiCustomMessagesMeetupMessage meetup : meetupsList.getMeetups()) {
                    MeetupObject mo = new MeetupObject(meetup.getName(), meetup.getOwner(), meetup.getActive(),accepted);
                    if(!meetupObjectsList.contains(mo))
                        meetupObjectsList.add(mo);
                }
                adapter=new MeetupListAdapter(getActivity(),meetupObjectsList);
            }
            else {
                if(accepted)
                    Log.v(TAG, "No accepted meetups");
                else
                    Log.v(TAG, "No pending meetups ");
            }
        }

        if(adapter!=null && !adapter.isEmpty()) {
//            Log.v(TAG,"adapter no null not empty");
            setListAdapter(adapter);
        }
        else{
            setListShown(true);
            if(accepted==true) {
                Toast.makeText(getActivity(), "You aren't involved in any meetups", Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "Start your own by clicking the plus", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getActivity(), "You have no pending meetup invites", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
