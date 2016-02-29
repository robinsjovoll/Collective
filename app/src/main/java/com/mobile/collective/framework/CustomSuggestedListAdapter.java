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
public class CustomSuggestedListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;

    public CustomSuggestedListAdapter(Activity context, String[] itemname) {
        super(context, R.layout.list_suggest_task, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_suggest_task, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.suggestedTaskName);
        TextView extratxt = (TextView) rowView.findViewById(R.id.suggestedTaskScore);

        txtTitle.setText(itemname[position]);
        extratxt.setText("Description "+itemname[position]);
        return rowView;

    };
}
