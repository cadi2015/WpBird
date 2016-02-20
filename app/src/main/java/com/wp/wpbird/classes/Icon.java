package com.wp.wpbird.classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import com.wp.wpbird.tools.IconReader;

import java.io.IOException;

/**
 * Created by wp on 2015/11/22.
 */
public class Icon {
    private final static String TAG = "Icon";
    private String mIconName;

    private float mScaleSize = 1;

    private int mWidth;
    private int mHeight;

    private double mStartX;
    private double mStartY;

    private double mEndX;
    private double mEndY;



    public Icon() {
        super();
    }

    public Bitmap getBitmap(Context context) {
        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeStream(context.getAssets().open("atlas.png"));
            Matrix matrix = new Matrix();
            matrix.postScale(mScaleSize, mScaleSize);
            Log.d(TAG,  "width = " + mWidth + " height = " + mHeight + " StartX = " + mStartX + " StartY = " + mStartY + " endX = " + mEndX + " endY = " + mEndY);
            bitmap = Bitmap.createBitmap(bitmap, (int) (mStartX * IconReader.PICTURE_WIDTH + 0.5) , (int) (mStartY * IconReader.PICTURE_HEIGHT + 0.5), mWidth, mHeight, matrix, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }



    public void setIconName(String IconName) {
        this.mIconName = IconName;
    }

    public String getIconName() {
        return mIconName;
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setStartX(double startX) {
        this.mStartX = startX;
    }

    public double getStartX() {
        return mStartX;
    }

    public void setStartY(double startY) {
        this.mStartY = startY;
    }

    public double getStartY() {
        return  mStartY;
    }

    public void setEndX(double endX) {
        this.mEndX = endX;
    }

    public double getEndX() {
        return mEndX;
    }

    public void setEndY(double endY) {
        this.mEndY = endY;
    }

    public double getEndY() {
        return mEndY;
    }


    public Icon setScaleSize(float scaleSize) {
        this.mScaleSize = scaleSize;
        return this;
    }

}
