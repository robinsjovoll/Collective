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
public class CustomSuggestedListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] suggestedTaskScores;
    private final String[] suggestedTaskNames;


    public CustomSuggestedListAdapter(Activity context, String[] suggestedTaskNames, String[] suggestedTaskScores) {
        super(context, R.layout.list_suggest_task, suggestedTaskNames);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.suggestedTaskNames=suggestedTaskNames;
        this.suggestedTaskScores = suggestedTaskScores;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_suggest_task, null,true);

        TextView taskName = (TextView) rowView.findViewById(R.id.suggestedTaskName);
        TextView taskScore = (TextView) rowView.findViewById(R.id.suggestedTaskScore);

        taskName.setText(suggestedTaskNames[position]);
        taskScore.setText("Score: " + suggestedTaskScores[position]);
        return rowView;

    };
}
