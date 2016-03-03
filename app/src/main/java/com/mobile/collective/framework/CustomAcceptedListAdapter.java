package com.mobile.collective.framework;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobile.collective.R;

import java.util.HashMap;

/**
 * Created by Robin on 29.02.2016.
 */
public class CustomAcceptedListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] acceptedTaskNames;
    private final String[] acceptedTaskScores;

    public CustomAcceptedListAdapter(Activity context, String[] acceptedTaskNames, String[] acceptedTaskScores) {
        super(context, R.layout.list_do_task, acceptedTaskNames);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.acceptedTaskNames=acceptedTaskNames;
        this.acceptedTaskScores = acceptedTaskScores;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_do_task, null,true);

        TextView taskName = (TextView) rowView.findViewById(R.id.acceptedTaskName);
        TextView taskScore = (TextView) rowView.findViewById(R.id.acceptedTaskScore);

        taskName.setText(acceptedTaskNames[position]);
        taskScore.setText("Score: "+acceptedTaskScores[position]);
        return rowView;

    };
}
