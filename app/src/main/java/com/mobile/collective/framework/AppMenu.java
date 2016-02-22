package com.mobile.collective.framework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Robin on 17/02/2016.
 */
public abstract class AppMenu extends AppCompatActivity {
    private static AndroidFileIO fileIO;

    @Override
    public void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
    }


    public void goToNoAnimation(Class javaClass) {
        Intent intent = new Intent(this, javaClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }

    /**
     * Sends the user to the specified screen.
     * @param javaClass
     */
    public void goTo(Class javaClass) {
        Intent intent = new Intent(this, javaClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish(); //Ends the previous activity
        overridePendingTransition(0, 0);
    }

    private String getStringResourceByName(String aString) {
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getString(resId);
    }

    public static AndroidFileIO getFileIO() {
        return fileIO;
    }
    public static void setFileIO(AndroidFileIO fileIO) {
        AppMenu.fileIO = fileIO;
    }

}
