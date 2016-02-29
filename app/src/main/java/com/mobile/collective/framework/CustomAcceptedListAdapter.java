package com.mobile.collective.framework;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobile.collective.R;

/**
 * Created by Robin on 29.02.2016.
 */
public class CustomAcceptedListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;

    public CustomAcceptedListAdapter(Activity context, String[] taskName) {
        super(context, R.layout.list_do_task, taskName);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=taskName;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_do_task, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.acceptedTaskName);
        TextView extratxt = (TextView) rowView.findViewById(R.id.acceptedTaskScore);

        txtTitle.setText(itemname[position]);
        extratxt.setText("Description "+itemname[position]);
        return rowView;

    };
}
