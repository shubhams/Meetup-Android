package iiitd.ac.in.dsys.meetup.CustomUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;

import iiitd.ac.in.dsys.meetup.ObjectClasses.ContactObject;
import iiitd.ac.in.dsys.meetup.R;

/**
 * Created by vedantdasswain on 25/03/15.
 */
public class ContactsListAdapter extends ArrayAdapter {
    private final Context context;
    private final ArrayList<ContactObject> entryObjectList;
    CompoundButton.OnCheckedChangeListener listener;

    public ContactsListAdapter(Context context, ArrayList<ContactObject> objectList,
                               CompoundButton.OnCheckedChangeListener listener) {
        super(context, R.layout.list_item_meetup, objectList);
        this.context = context;
        this.entryObjectList = objectList;
        this.listener=listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item_contact, parent, false);
        CheckBox contactCB = (CheckBox) rowView.findViewById(R.id.contactCheckBox);
        contactCB.setText(entryObjectList.get(position).getName());
        contactCB.setChecked(entryObjectList.get(position).getInvited());
        contactCB.setOnCheckedChangeListener(listener);
//        TextView timeTextView = (TextView)rowView.findViewById(R.id.timeTV);
//        timeTextView.setText(""+entryObjectList.get(position).getTimeOfArrival());
        return rowView;
    }
}
