package com.mobile.collective.framework;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.collective.R;

public class CustomScoreListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] scoreUsers, scoreScores;
    private String[] colorArray;

    public CustomScoreListAdapter(Activity context, String[] scoreUsers, String[] scoreScores, String[] colorArray) {
        super(context, R.layout.list_scores, scoreUsers);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.scoreUsers=scoreUsers;
        this.scoreScores = scoreScores;
        this.colorArray = colorArray;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_scores, null, true);

        TextView userScoreTextView = (TextView) rowView.findViewById(R.id.user);
        TextView scoreScoreTextView = (TextView) rowView.findViewById(R.id.userScore);
        ImageView scoreBarImageView = (ImageView) rowView.findViewById(R.id.scoreBar);


//        Log.e("Custom", "position: " + position); 2016-03-05T16:12:50.235Z

        scoreBarImageView.setMinimumWidth(Integer.parseInt(scoreScores[position]));
        scoreBarImageView.setBackgroundColor(Color.parseColor(colorArray[position]));
        userScoreTextView.setText(scoreUsers[position]);
        scoreScoreTextView.setText(scoreScores[position] + "p");
        return rowView;
    };
}