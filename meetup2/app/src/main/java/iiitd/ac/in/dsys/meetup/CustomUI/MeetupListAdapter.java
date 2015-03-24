package iiitd.ac.in.dsys.meetup.CustomUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import iiitd.ac.in.dsys.meetup.Objects.MeetupObject;
import iiitd.ac.in.dsys.meetup.R;

/**
 * Created by vedantdasswain on 24/03/15.
 */
public class MeetupListAdapter extends ArrayAdapter {
    private final Context context;
    private final ArrayList<MeetupObject> entryObjectList;

    public MeetupListAdapter(Context context, ArrayList<MeetupObject> objectList) {
        super(context, R.layout.meetup_list_item, objectList);
        this.context = context;
        this.entryObjectList = objectList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.meetup_list_item, parent, false);
        TextView nameTextView = (TextView)rowView.findViewById(R.id.nameTV);
        nameTextView.setText(entryObjectList.get(position).getName());
        TextView timeTextView = (TextView)rowView.findViewById(R.id.timeTV);
        timeTextView.setText(""+entryObjectList.get(position).getTimeOfArrival());
        TextView ownerTextView = (TextView) rowView.findViewById(R.id.ownerTV);
        ownerTextView.setText(entryObjectList.get(position).getOwner());
        Switch sw=(Switch) rowView.findViewById(R.id.switch1);
        sw.setChecked(entryObjectList.get(position).getActive());
        return rowView;
    }
}
