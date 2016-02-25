package com.mobile.collective.implementation.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.mobile.collective.R;
import com.mobile.collective.framework.AppMenu;

public class FlatmatesView extends AppMenu {

    TextView displayRights;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flatmates);

        displayRights = (TextView)findViewById(R.id.display_rights);

        /*if(this.user.isAdmin()) {
            displayRights.setText("(Admin view)");
        }
        else {
            displayRights.setText("(User view)");
        }*/


    }




}
