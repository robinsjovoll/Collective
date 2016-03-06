package com.mobile.collective.framework;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.mobile.collective.R;

/**
 * Created by Robin on 29.02.2016.
 */
public class CustomTaskHistoryListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] taskHistoryUsernames;
    private final String[] taskHistoryDates;


    public CustomTaskHistoryListAdapter(Activity context, String[] taskHistoryUsernames, String[] taskHistoryDates) {
        super(context, R.layout.list_task_history, taskHistoryUsernames);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.taskHistoryUsernames=taskHistoryUsernames;
        this.taskHistoryDates = taskHistoryDates;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_task_history, null, true);

        TextView taskHistoryName = (TextView) rowView.findViewById(R.id.usernameHistory);
        TextView taskHistoryDate = (TextView) rowView.findViewById(R.id.dateHistory);

//        Log.e("Custom", "position: " + position); 2016-03-05T16:12:50.235Z
        String date = taskHistoryDates[position].substring(8,10) + "-" + taskHistoryDates[position].substring(5,7) + "-" + taskHistoryDates[position].substring(0,4);
        String time = taskHistoryDates[position].substring(11,16);
        taskHistoryName.setText("Bruker: " + taskHistoryUsernames[position]);
        taskHistoryDate.setText("Dato: " + date + ", Klokkeslett: " + time);
        return rowView;

    };
}
