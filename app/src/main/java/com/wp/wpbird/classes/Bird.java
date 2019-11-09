package com.wp.wpbird.classes;

import android.content.Context;
import android.graphics.Bitmap;

import com.wp.wpbird.config.GameConfig;
import com.wp.wpbird.tools.IconReader;
import com.wp.wpbird.tools.IconSizeManager;
import com.wp.wpbird.tools.ScreenManager;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wp on 2015/12/6.
 */
public class Bird {
    private final static String TAG = "Bird";
    private boolean mGameBeginning = false;

    private IconReader mIconReader;
    private Bitmap[] mBirds;
    private Context mContext;

    private int mRanNum; //用于修改图片的索引值
    private int[] mCountNums = {0, 1, 2, 1}; //一个数组，有4个元素，每个元素都是int，这里是直接初始化数组实例对象，外加每个元素的实例对象（基本数据类型就是值）
    private int mCount; //用于标记得到mCountNums的索引值
    private int mCountDelay = 2;

    private int[] mFlyingNums = {1, -1, -1, 1};
    private int mFlyCount;
    private int mFlyDelay;//用于小鸟上下飞延迟用

    private Timer mTimer;
    private int BIRD_DELAY = 39; //小鸟属性更新频率
    private final int BIRD_GRAVITY = 1;
    private int mBirdSpeed;
    private int mBirdAngle; //小鸟角度
    private int mBirdHeight;
    private int mBirdBitmapHeight;
    private int mBirdBimmapWidth;

    public Bird(Context context) {
        super();
        mContext = context;
        init();
    }

    public void init() {
        mIconReader = IconReader.getInstance(mContext);
        mBirds = new Bitmap[3];
        Random random = new Random();
        int ranNum = random.nextInt(3);
        for (int i = 0; i < mBirds.length; i++) {
            mBirds[i] = mIconReader.getIcon("bird" + ranNum + "_" + i).setScaleSize(IconSizeManager.ICON_SIZE).getBitmap(mContext);
        }
        mRanNum = 0;
        mBirdAngle = 0; //初始化小鸟角度
        mCount = 0;
        mFlyCount = 0;
        mFlyDelay = 3; //用于延迟飞行
        mBirdHeight = IconSizeManager.BIRD_INIT_LOC_Y;
        mBirdSpeed = 3;
        mTimer = new Timer();
        mBirdBitmapHeight = mBirds[1].getHeight();
        mBirdBimmapWidth = mBirds[1].getWidth();
        tenesmus();
    }

    public int getBirdBitmapHeight(){
        return mBirdBitmapHeight;
    }

    public int getBirdBitMapWidth(){
        return mBirdBimmapWidth;
    }

    public Bitmap getBird() {
//        Log.v(TAG, "getBird[? = ]" + mRanNum );
        return mBirds[mRanNum];
    }

    public void tenesmus() {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mGameBeginning) {
                    mBirdSpeed = mBirdSpeed + BIRD_GRAVITY;
                    if (!GameConfig.debug) {
                        mBirdHeight += mBirdSpeed;
                    }
                    mBirdAngle += 2;

                    if (mBirdAngle > 90) {
                        mBirdAngle = 90;
                    }
                } else {
                    if (mFlyDelay-- == 0) {
                        mFlyDelay = 3;
                        mBirdHeight += mFlyingNums[mFlyCount = (mFlyCount + 1) % 4] * (int) (3 * IconSizeManager.ICON_SIZE);
                    }
                }

                if (mCountDelay-- == 0) {
                    mCountDelay = 2;
                    mRanNum = mCountNums[mCount = (mCount + 1) % 4]; //好嘛，用取模的方式 就能拿到 0、1、2、3四个索引，然后就能取值0、1、2、1,这么的取到值了
                }

            }
        }, 0, BIRD_DELAY);
    }


    public int getBirdHeight() {
        return mBirdHeight;
    }


    /*
    public int getBirdWidth () {
        int width = mBirds[0].getWidth();

        return width;
    }
    */

    public int getBirdAngle() {
        return mBirdAngle;
    }


    public void jump() {
        mBirdHeight = mBirdHeight - IconSizeManager.BIRD_JUMP_HEIGHT;  //改变绘制小鸟的高度 ，即Y轴坐标，GameView在不断绘制，看起来就像鸟飞起来一样
        mBirdAngle = -30; //改变小鸟的角度
        mBirdSpeed = 3;  //速度重新变为3
        if (mBirdHeight <= -IconSizeManager.BIRD_WIDTH) {
            mBirdHeight = -IconSizeManager.BIRD_WIDTH;
        }
    }

    public void setGameBeginning(boolean isStart) {
        this.mGameBeginning = isStart;
    }

    public void die() {
        mGameBeginning = false;
        mTimer.cancel();

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(mBirdHeight + IconSizeManager.BIRD_REAL_WIDTH > ScreenManager.SCREEN_HEIGHT - IconSizeManager.GROUND_HEIGHT) {
                    mBirdHeight = ScreenManager.SCREEN_HEIGHT
                            - IconSizeManager.GROUND_HEIGHT
                            - IconSizeManager.BIRD_REAL_WIDTH
                            - IconSizeManager.BIRD_WIDTH_SPACE
                            + (int) (3 * IconSizeManager.ICON_SIZE);
                    mTimer.cancel();
                }
                mBirdAngle = 90;
                mBirdHeight += 10;
            }
        }, 0, 10);

    }

}
