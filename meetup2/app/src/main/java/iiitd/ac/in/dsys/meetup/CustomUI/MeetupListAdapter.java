package iiitd.ac.in.dsys.meetup.CustomUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import iiitd.ac.in.dsys.meetup.ObjectClasses.MeetupObject;
import iiitd.ac.in.dsys.meetup.R;

/**
 * Created by vedantdasswain on 24/03/15.
 */
public class MeetupListAdapter extends ArrayAdapter {
    private static final String TAG ="MeetupListAdapter" ;
    private final Context context;
    private final ArrayList<MeetupObject> entryObjectList;

    public MeetupListAdapter(Context context, ArrayList<MeetupObject> objectList) {
        super(context, R.layout.list_item_meetup, objectList);
        this.context = context;
        this.entryObjectList = objectList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item_meetup, parent, false);
        TextView nameTextView = (TextView)rowView.findViewById(R.id.nameTV);
        nameTextView.setText(entryObjectList.get(position).getName());

        if(!entryObjectList.get(position).getAccepted())
            nameTextView.setTextColor(context.getResources()
                    .getColor(R.color.dim_foreground_disabled_material_light));

        TextView ownerTextView = (TextView) rowView.findViewById(R.id.ownerTV);
        ownerTextView.setText(entryObjectList.get(position).getOwner());
        Switch sw=(Switch) rowView.findViewById(R.id.switch1);
        sw.setChecked(entryObjectList.get(position).getActive());
        return rowView;
    }

}
