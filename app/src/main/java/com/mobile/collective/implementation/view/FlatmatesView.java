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
import com.mobile.collective.implementation.controller.MainMenuController;
import com.mobile.collective.implementation.model.User;

public class FlatmatesView extends AppMenu {

    TextView displayRights;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flatmates);

        displayRights = (TextView) findViewById(R.id.display_rights);

        System.out.println(checkUserRights());
        System.out.println(getUser().isAdmin());
    }
    public void goToMainView(View v)
    {
        goTo(MainMenuController.class);
    }
    public boolean checkUserRights() {

        try {
            if (getUser().isAdmin()) {
                displayRights.setText("Admin-innsyn");
            }
            else {
                displayRights.setText("Bruker-innsyn");
            }
            return true;
        } catch(NullPointerException ne) {
            ne.printStackTrace();
            displayRights.setText("Ukjent brukerstatus");
        }

    return false;

    }




}
