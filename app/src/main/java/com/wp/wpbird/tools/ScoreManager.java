package com.wp.wpbird.tools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by wp on 2016/2/15.
 */
public class ScoreManager {
    public static SharedPreferences preferences;
    private static SharedPreferences.Editor editor; //Editor内部静态接口引用对象

    public static void init(Context context) {
        preferences = context.getSharedPreferences("flappy_bird_best_score", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static void setBestScore(int bestScore) {
        editor.putInt("bestScore", bestScore);
        editor.commit();
    }

}
