package com.wp.wpbird.classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.wp.wpbird.tools.IconReader;
import com.wp.wpbird.tools.IconSizeManager;
import com.wp.wpbird.tools.ScreenManager;

/**
 * Created by wp on 2016/1/2.
 */
public class Pillar {
    private static final String TAG = "Pillar";
    private static IconReader mIconReader; //怪不得用static，用静态变量好啊，所有实例对象，用的都是同一块内存，即同一个IconReader
    private static Bitmap mBitmapNorPill;   // 这里也是所有的实例对象 用的同一个Bitmap就好
    private int mLoc_X;
    private int mLoc_Y;
    private boolean mIsPassed;  //加个标志位，解决无限++的bug
    public Pillar(Context context) {
        super();
        init(context);
    }

    private void init(Context context) {
        mIconReader = IconReader.getInstance(context);
        if (mBitmapNorPill == null) {
            mBitmapNorPill = readBitmap(context, "pipe_down", "pipe_up"); //就在类第一次加载的时候(第一次创建实例对象的时候），读取图片，就行了，不然有卡顿的bug，每次你都
        }
        mLoc_X = ScreenManager.SCREEN_WIDTH;
        mLoc_Y =  - (int)((Math.random() * 10) * 80); //Y轴利用随机值，每次生成的实例对象所获的值是随机的，就会造成柱子每次都是不同的情况
        mIsPassed = false;
        Log.d(TAG, "mLoc_Y = " + mLoc_Y);
    }


    private Bitmap readBitmap(Context context, String directDown, String directUp) {
        Bitmap directDownBit = mIconReader.getIcon(directDown).setScaleSize(IconSizeManager.ICON_SIZE).getBitmap(context);
        Bitmap directUpBit = mIconReader.getIcon(directUp).setScaleSize(IconSizeManager.ICON_SIZE).getBitmap(context);
        int wid = directDownBit.getWidth();
        int hei = directDownBit.getHeight() * 2 + IconSizeManager.PILLAR_GAP_HEIGHT;
        Bitmap diyCanvas = Bitmap.createBitmap(wid, hei, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(diyCanvas);
        canvas.drawBitmap(directDownBit, 0, 0, null);
        canvas.drawBitmap(directUpBit, 0,directDownBit.getHeight() + IconSizeManager.PILLAR_GAP_HEIGHT, null);
        return diyCanvas;
    }

    public Bitmap getBitmapNorPill() {
        return mBitmapNorPill;
    }


    public int getLoc_X() {
        return mLoc_X;
    }

    public int getLoc_Y() {
        return mLoc_Y;
    }

    public void movePill(int gameSpeed) {
        mLoc_X -= (int) (IconSizeManager.PILLAR_SPEED * gameSpeed * 1.0 / 1000 ); //移动柱子，就是改变它的X轴坐标位置
    }

    public boolean isPassed() {
        return mIsPassed;
    }

    public void setPassed(boolean isPassed) {
        mIsPassed = isPassed;
    }
}
