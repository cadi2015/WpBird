package com.wp.wpbird.tools;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by wp on 2015/11/28.
 */
public class BitmapChanger {

    public static Bitmap changeSize(Bitmap bitmap, int screenWidth, int screenHeight) {
        int currentWidth = bitmap.getWidth();
        int currentHeight = bitmap.getHeight();
        float sX = (float)screenWidth / currentWidth;
        float sY = (float)screenHeight / currentHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(sX, sY);  //相当于缩放
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, currentWidth, currentHeight, matrix, true);
        return bitmap;
    }
}
