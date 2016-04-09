package com.mobile.collective.framework;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mobile.collective.R;

import org.w3c.dom.Text;

/**
 * Created by Robin on 29.02.2016.
 */
public class CustomFlatMatesListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] usernames;
    private final String[] emails;
    private final String personalUsername;
    private final boolean isAdmin;


    public CustomFlatMatesListAdapter(Activity context, String[] usernames, String personalUsername, boolean isAdmin, String[] emails) {
        super(context, R.layout.list_flatmates, usernames);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.usernames = usernames;
        this.personalUsername = personalUsername;
        this.isAdmin = isAdmin;
        this.emails = emails;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_flatmates, null, true);

        TextView usernameTextView = (TextView) rowView.findViewById(R.id.flatmatesUsername);
        TextView emailTextView = (TextView) rowView.findViewById(R.id.flatMatesEmail);
        ImageButton promote = (ImageButton) rowView.findViewById(R.id.upgrade_to_admin);
        ImageButton leave = (ImageButton) rowView.findViewById(R.id.leave_flat);
        if(!isAdmin){
            promote.setVisibility(View.INVISIBLE);
            leave.setVisibility(View.INVISIBLE);
            if(usernames[position].equals(personalUsername)){
                leave.setVisibility(View.VISIBLE);
            }
        }else {
            if (usernames[position].equals(personalUsername)) {
                promote.setVisibility(View.INVISIBLE);
            }
        }
        emailTextView.setText(emails[position]);
        usernameTextView.setText(usernames[position]);

        return rowView;

    };
}
