package com.mobile.collective.framework;

import android.app.Activity;
import android.util.Log;
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
public class CustomHistoryListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private String[] historyUsernames;
    private String[] historyTaskDates;
    private String[] historyTaskNames;
    private String[] historyTaskScores;


    public CustomHistoryListAdapter(Activity context, String[] historyUsernames, String[] historyTaskDates, String[] historyTaskNames, String[] historyTaskScores) {
        super(context, R.layout.list_history_feed, historyUsernames);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.historyUsernames=historyUsernames;
        this.historyTaskDates = historyTaskDates;
        this.historyTaskNames = historyTaskNames;
        this.historyTaskScores = historyTaskScores;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_history_feed, null, true);

        TextView dateHistory = (TextView) rowView.findViewById(R.id.dateHistory);
        TextView userHistory = (TextView) rowView.findViewById(R.id.userCompletedTask);

        String date = historyTaskDates[position].substring(8,10) + "-" + historyTaskDates[position].substring(5,7) + "-" + historyTaskDates[position].substring(0,4);
        String time = historyTaskDates[position].substring(11,16);

        dateHistory.setText("Dato: " + date + "\nKlokkeslett: " + time);
        userHistory.setText(historyUsernames[position] + " utførte oppgave:\n" + historyTaskNames[position] + " (" + historyTaskScores[position] + " p)");

        return rowView;

    };
}
