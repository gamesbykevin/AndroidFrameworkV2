package com.gamesbykevin.androidframeworkv2.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.IntBuffer;
import java.util.Date;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Kevin on 7/31/2017.
 */

public class UtilityHelper {

    /**
     * Are we debugging the application
     */
    public static boolean DEBUG = false;

    /**
     * Are we running unit tests
     */
    public static boolean UNIT_TEST = false;

    /**
     * App name for our framework
     */
    public static final String TAG = "AndroidFrameworkV2";

    //Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    public static void handleException(final Exception exception) {

        //if not debugging, don't continue
        if (!DEBUG)
            return;

        if (UNIT_TEST) {
            System.out.print(exception);
        } else {
            //log as error
            Log.e(TAG, exception.getMessage(), exception);
        }

        //handle process
        exception.printStackTrace();
    }

    public static void logEvent(final String message) {

        //if not debugging, don't continue
        if (!DEBUG)
            return;

        //don't do anything if null
        if (message == null)
            return;

        //length limit of each line we print
        int maxLogSize = 4000;

        //if the string is too long
        if (message.length() > maxLogSize) {

            //we will display a portion at a time
            for(int i = 0; i <= message.length() / maxLogSize; i++) {

                int start = i * maxLogSize;
                int end = (i+1) * maxLogSize;
                end = end > message.length() ? message.length() : end;

                if (UNIT_TEST) {
                    System.out.println(message.substring(start, end));
                } else {
                    Log.i(TAG, message.substring(start, end));
                }
            }

        } else {

            if (UNIT_TEST) {
                System.out.println(message);
            } else {
                //log string as information
                Log.i(TAG, message);
            }
        }
    }

    public static void displayMessage(final Context context, final String message) {

        //if not debugging, don't continue
        if (!DEBUG)
            return;

        //show text
        Toast.makeText(context, message , Toast.LENGTH_SHORT).show();
    }

    /**
     * Take a screenshot and write file to external phone storage
     * @param activity Required to determine if we have permission to write
     * @param openGL Open GL Context containing screen pixel data
     * @param x Starting x-coordinate of screenshot
     * @param y Starting y-coordinate of screenshot
     * @param w Width to capture
     * @param h Height to capture
     * @return true if successful creating screenshot false otherwise
     */
    public static boolean takeScreenshotOpenGL(Activity activity, GL10 openGL, int x, int y, int w, int h) {
        return takeScreenshotOpenGL(activity, openGL, x, y, w, h, null);
    }

    /**
     * Take a screenshot and write file to external phone storage
     * @param activity Required to determine if we have permission to write
     * @param openGL Open GL Context containing screen pixel data
     * @param x Starting x-coordinate of screenshot
     * @param y Starting y-coordinate of screenshot
     * @param w Width to capture
     * @param h Height to capture
     * @param fileName Do you want a specific file name? If null default timestamp will be used
     * @return true if successful creating screenshot false otherwise
     */
    public static boolean takeScreenshotOpenGL(Activity activity, GL10 openGL, int x, int y, int w, int h, String fileName) {
        try {
            int b[] = new int[w * (y + h)];
            int bt[] = new int[w * h];
            IntBuffer ib = IntBuffer.wrap(b);
            ib.position(0);
            openGL.glReadPixels(x, 0, w, y + h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, ib);

            //remember, that OpenGL bitmap is incompatible with Android bitmap and so, some correction need.
            for (int i = 0, k = 0; i < h; i++, k++) {
                for (int j = 0; j < w; j++) {
                    int pix = b[i * w + j];
                    int pb = (pix >> 16) & 0xff;
                    int pr = (pix << 16) & 0x00ff0000;
                    int pix1 = (pix & 0xff00ff00) | pr | pb;
                    bt[(h - k - 1) * w + j] = pix1;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(bt, w, h, Bitmap.Config.ARGB_8888);

            //return result of saving bitmap
            return saveBitmap(activity, bitmap, fileName);

        } catch (Exception e) {

            handleException(e);

            //something went wrong
            return false;
        }
    }

    public static boolean takeScreenshotActivity(Activity activity, int resId, int x, int y, int w, int h, String fileName) {

        try {
            View screenView = activity.getWindow().getDecorView().findViewById(resId).getRootView();
            screenView.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
            screenView.setDrawingCacheEnabled(false);

            return saveBitmap(activity, bitmap, fileName);
        } catch (Exception e) {
            UtilityHelper.handleException(e);
            return false;
        }
    }

    private static boolean saveBitmap(Activity activity, Bitmap bitmap, String fileName) {

        try {
            //if marshmallow or later, we need to verify permission has been given
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                verifyStoragePermissions(activity);

            String mPath;

            //if no filename provided, default to timestamp
            if (fileName == null || fileName.trim().length() < 2) {
                Date now = new Date();
                android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
                mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";
            } else {
                mPath = Environment.getExternalStorageDirectory().toString() + "/" + fileName + ".jpg";
            }

            File imageFile = new File(mPath);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (Exception e) {
            handleException(e);
            return false;
        }
    }

    /**
     * Check if our application has permission write to the device storage.<br>
     * If no permission, the user will be prompted to grant permissions
     * @param activity Activity references needed to check our permissions
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}