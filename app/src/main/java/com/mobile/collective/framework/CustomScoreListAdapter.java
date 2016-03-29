package com.mobile.collective.framework;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.collective.R;

public class CustomScoreListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] scoreUsers, scoreScores;
    private String[] colorArray;
    private int totalScore;

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

        int userBarWidth = convertScorePercentageToBarWidth(Integer.parseInt(scoreScores[position]), getTotalScore(), getScreenWidth());
        scoreBarImageView.setMinimumWidth(userBarWidth);
        scoreBarImageView.setBackgroundColor(Color.parseColor(colorArray[position]));
        userScoreTextView.setText(scoreUsers[position]);
        scoreScoreTextView.setText(scoreScores[position] + "p");
        return rowView;
    };

    public int getScreenWidth(){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width;
    }

    public int getTotalScore(){
        int total = 0;
        for(int i = 0; i < scoreScores.length; i++)
        {
            total += Integer.parseInt(scoreScores[i]);
        }
        return total;
    }

    public int convertScorePercentageToBarWidth(int userScore, int totalScore, int screenWidth){
        int totalScreenWidth = screenWidth;
        int userBarWidth = 0;
        int userScorePercentage = 0;

        System.out.println("userScore: " + userScore);
        System.out.println("totalScore: " + totalScore);
        System.out.println("screenWidth: " + screenWidth);

        userScorePercentage = (int) Math.round(userScore * 100.0/totalScore);
        userBarWidth = (int) Math.round((userScorePercentage / 100.0) * totalScreenWidth );

        System.out.println("userScorePercentage: " + userScorePercentage);
        System.out.println("userBarWidth: " + userBarWidth);

        return userBarWidth;
    }
}
