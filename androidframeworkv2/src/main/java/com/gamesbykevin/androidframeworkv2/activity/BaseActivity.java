package com.gamesbykevin.androidframeworkv2.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;

import com.gamesbykevin.androidframeworkv2.R;
import com.gamesbykevin.androidframeworkv2.base.Disposable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Created by Kevin on 8/1/2017.
 */

public abstract class BaseActivity extends Activity implements Disposable {

    //where we store our game option selections
    private static SharedPreferences preferences;

    //our vibrate object
    private static Vibrator vibrator;

    //the intent used to open web urls
    private Intent intent;

    /**
     * The default duration of the vibration
     */
    public static final long VIBRATE_DURATION = 500L;

    //gson object to retrieve and convert json string to object
    public static Gson GSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //call parent
        super.onCreate(savedInstanceState);

        //create new instance if null
        if (GSON == null) {

            //use to create custom GSON object
            GsonBuilder gsonBuilder = new GsonBuilder();

            //need to turn on to support our complex hash map
            gsonBuilder.enableComplexMapKeySerialization();

            //get our object
            GSON = gsonBuilder.create();
        }

        //create our shared preferences object and make sure we have key default values entered
        if (this.preferences == null)
            this.preferences = super.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        //create our vibrator object
        if (this.vibrator == null)
            this.vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
    }

    /**
     * Get the shared preferences
     * @return The object where we store our game options
     */
    public static SharedPreferences getSharedPreferences() {
        return preferences;
    }

    /**
     * Get the boolean value of the shared preferences setting
     * @param key The unique key of the setting we want to retrieve
     * @return The value of the setting for the corresponding key, if not set true will be returned by default
     */
    public boolean getBooleanValue(final int key) {
        return getSharedPreferences().getBoolean(getString(key), true);
    }

    /**
     * Get our object
     * @param key The unique key of the setting we want to retrieve
     * @param classObj The class instance of the object we want to retrieve (could be enum)
     * @return object reference based on the shared preference setting
     */
    public Object getObjectValue(final int key, final Class classObj) {

        //convert from json to object
        return GSON.fromJson(getSharedPreferences().getString(getString(key), ""), classObj);
    }

    /**
     * Get our object
     * @param key The unique key of the setting we want to retrieve
     * @param type The class instance type, needed to de-serialize a json string back to hash map
     * @return object reference based on the shared preference setting
     */
    public Object getObjectValue(final int key, final Type type) {

        //convert from json to object
        return GSON.fromJson(getSharedPreferences().getString(getString(key), ""), type);
    }

    /**
     * Vibrate the phone for the default duration, if vibrate is enabled in shared preferences
     */
    public void vibrate() {
        vibrate(VIBRATE_DURATION, false);
    }

    /**
     * Vibrate the phone for the default duration
     * @param ignoreSetting Do we ignore the shared preferences vibrate setting?
     */
    public void vibrate(boolean ignoreSetting) {
        vibrate(VIBRATE_DURATION, ignoreSetting);
    }

    /**
     * Vibrate the phone for the specified duration
     * @param milliseconds The duration of the vibrate
     * @param ignoreSetting Do we ignore the shared preferences vibrate setting?
     */
    public void vibrate(long milliseconds, boolean ignoreSetting) {

        if (ignoreSetting) {
            //if we ignore the shared preferences setting vibrate anyways
            vibrator.vibrate(milliseconds);
        } else if (!ignoreSetting && getBooleanValue(R.string.vibrate_file_key)) {
            //if we want to honor the shared preferences setting and it is enabled
            vibrator.vibrate(milliseconds);
        }
    }

    /**
     * Release all resources in BaseActivity
     */
    @Override
    public void dispose() {

        //set objects null
        preferences = null;
        vibrator = null;
        GSON = null;
    }

    /**
     * Open the specified url in the mobile web browser
     * @param url Desired website
     */
    protected void openUrl(final String url) {

        //if not established create the intent
        if (this.intent == null) {
            this.intent = new Intent(Intent.ACTION_VIEW);
        }

        //set the url
        this.intent.setData(Uri.parse(url));

        //start the activity opening the app / web browser
        startActivity(this.intent);
    }

    @Override
    protected void onStart() {

        //call parent
        super.onStart();
    }

    @Override
    protected void onDestroy() {

        //call parent
        super.onDestroy();
    }

    @Override
    protected void onPause() {

        //call parent
        super.onPause();
    }

    @Override
    protected void onResume() {

        //call parent
        super.onResume();
    }
}