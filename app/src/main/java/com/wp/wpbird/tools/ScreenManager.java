package com.wp.wpbird.tools;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by wp on 2015/11/28.
 */
public class ScreenManager {
    private static final String TAG = "ScreenManager";
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static float SCREEN_DENSITY;



    public static void init(WindowManager manager) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(displayMetrics); //拿到的屏幕参数，都放到DisplayMetrics对象里
        SCREEN_WIDTH = displayMetrics.widthPixels;
        SCREEN_HEIGHT = displayMetrics.heightPixels;
        SCREEN_DENSITY = displayMetrics.density;
        Log.d(TAG, "SCR_WIDTH = " + SCREEN_WIDTH + " SCR_HEIGHT = " + SCREEN_HEIGHT + " SCR_density = " + SCREEN_DENSITY);
    }
}
