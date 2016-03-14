package com.wp.wpbird.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.wp.wpbird.activity.MainActivity;
import com.wp.wpbird.classes.Bird;
import com.wp.wpbird.classes.Pillar;
import com.wp.wpbird.tools.BitmapChanger;
import com.wp.wpbird.tools.IconReader;
import com.wp.wpbird.tools.IconSizeManager;
import com.wp.wpbird.tools.ScreenManager;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by wp on 2015/12/6.
 */
public class GameView extends View {
    private static final String TAG = "GameView";
    private Bird mBird;
    private Paint mPaint;
    private Context mContext;
    private Timer mTimer;
    private Handler mHandler;
    private final static int GAME_SPEED = 10;

    private IconReader mIconReader;
    private Bitmap mBitmapLand;

    private Bitmap[] mScoreNums;

    private Bitmap mBitmapGameOver;
    private Bitmap mBitmapReady; //游戏开始时的文案与引导
    private Bitmap mBitmapTutorial;
    private int mAlpha;

    private int mLand_loc_x = 0;

    private ArrayList<Pillar> mPillars;

    private int mGameState; //0代表游戏还未开始、 1代表游戏开始、2代表游戏结束

    private boolean mIsStarting;

    private int mGameScore;

    private boolean mNeedShowOver;

    /**
     * 这个构造方法呢，是在xml中初始化这个View实例对象的时候会调用的，所以啊，Android操作系统既然要生成这个实例对象
     * 你必须就得写这个构造方法，不然…………必须抛出异常哈
     *
     * @param context 传入Context实例对象
     * @param attrs   传入一个AttributeSet实例对象
     */
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs); //
        this.mContext = context;
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0x444) {
                    invalidate();
                }
            }
        };
        init();
        this.setOnClickListener(new MyClickLis());
    }

    private class MyClickLis implements View.OnClickListener {

        MyClickLis() {
            super();
        }

        @Override
        public void onClick(View v) {
            gameGoing();
        }
    }

    private void gameGoing() {

        if (mGameState == 0) {
            gameStart();
        }
        mBird.jump();
    }

    private void gameStart() {
        Log.d(TAG, "gameStart()");
        mAlpha -= 100;
        mBird.setGameBeginning(true);
        mIsStarting = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "gameStart() mGameState = " + mGameState);
                if (mGameState != 2) {
                    mGameState = 1; //目的是延迟出现水管
                }
            }
        }, 1500);  //1.5秒后出现水管
    }


    /**
     * View的绘制靠的就是这个接口
     *
     * @param canvas
     */

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        //绘制障碍物
        for (int i = 0; i < mPillars.size(); i++) {
            Pillar temp = mPillars.get(i);
            canvas.drawBitmap(temp.getBitmapNorPill(), temp.getLoc_X(), temp.getLoc_Y(), mPaint);
        }

        if (!mNeedShowOver) {
            canvas.drawBitmap(mScoreNums[mGameScore % 10], ScreenManager.SCREEN_WIDTH / 2 - mScoreNums[0].getWidth() / 2 + 30, ScreenManager.SCREEN_HEIGHT / 8, mPaint); //绘制得分
            if (mGameScore >= 10) {
                canvas.drawBitmap(mScoreNums[mGameScore / 10], ScreenManager.SCREEN_WIDTH / 2 - mScoreNums[0].getWidth(), ScreenManager.SCREEN_HEIGHT / 8, mPaint);
            }
        }


        if (mNeedShowOver) {
            canvas.drawBitmap(mBitmapGameOver, (ScreenManager.SCREEN_WIDTH - mBitmapGameOver.getWidth()) / 2, ScreenManager.SCREEN_HEIGHT / 8, mPaint);
        }

        //这是绘制小鸟的
        Matrix matrix = new Matrix();
        matrix.setRotate(mBird.getBirdAngle(), IconSizeManager.BIRD_TURNING_POINT_X, IconSizeManager.BIRD_TURNING_POINT_Y);  //好吧，旋转我真是醉了
        matrix.postTranslate(IconSizeManager.BIRD_LOC_X, mBird.getBirdHeight()); //平移，传入x轴坐标、y轴坐标，这里注意手机的左上角坐标是x = 0, y = 0,
        canvas.drawBitmap(mBird.getBird(), matrix, mPaint);  //利用Canvas的实例方法 drawBitmap

        //绘制地面，这里的算法我真的不明白
        if (mLand_loc_x <= 0 && mLand_loc_x >= ScreenManager.SCREEN_WIDTH - mBitmapLand.getWidth()) {
            canvas.drawBitmap(mBitmapLand, mLand_loc_x, ScreenManager.SCREEN_HEIGHT - IconSizeManager.GROUND_HEIGHT, mPaint);
        } else if (mLand_loc_x <= ScreenManager.SCREEN_WIDTH - mBitmapLand.getWidth() && mLand_loc_x >= -mBitmapLand.getWidth()) {
            canvas.drawBitmap(mBitmapLand, mLand_loc_x, ScreenManager.SCREEN_HEIGHT - IconSizeManager.GROUND_HEIGHT, mPaint);
            canvas.drawBitmap(mBitmapLand, mLand_loc_x + mBitmapLand.getWidth(), ScreenManager.SCREEN_HEIGHT - IconSizeManager.GROUND_HEIGHT, mPaint);
        } else {
            mLand_loc_x += mBitmapLand.getWidth();
            canvas.drawBitmap(mBitmapLand, mLand_loc_x, ScreenManager.SCREEN_HEIGHT - IconSizeManager.GROUND_HEIGHT, mPaint);
        }

        if (mAlpha >= 0) {
            Paint paint = new Paint();  //new一个Paint实例
            paint.setAlpha(mAlpha);           //设置透明度
            canvas.drawBitmap(mBitmapReady, (ScreenManager.SCREEN_WIDTH - mBitmapReady.getWidth()) / 2, IconSizeManager.BIRD_INIT_LOC_Y - mBird.getBird().getHeight() * 2, paint);
            canvas.drawBitmap(mBitmapTutorial, (ScreenManager.SCREEN_WIDTH - mBitmapTutorial.getWidth()) / 2, IconSizeManager.BIRD_INIT_LOC_Y, paint);
            if (mAlpha != 255) {
                mAlpha -= 10;
            }
        }
    }

    public void init() {
        this.setClickable(true);
        if (mBird == null) {
            mBird = new Bird(mContext);   //如果bird为空，new一个实例对象，不然的话，仍然复用原来的bird
        } else {
            mBird.init();
        }
        mPaint = new Paint();
        mTimer = new Timer();
        mIconReader = new IconReader(mContext);
        mGameState = 0;
        mGameScore = 0;
        mIsStarting = false;
        mNeedShowOver = false;
        mPillars = new ArrayList();
        mPillars.add(new Pillar(mContext));//一个Pillars实例对象当然不够用，我们用ArrayList管理多个实例对象
        if (mScoreNums == null) {
            mScoreNums = new Bitmap[10];
            for (int i = 0; i < mScoreNums.length; i++) {
                mScoreNums[i] = mIconReader.getIcon("font_0" + (48 + i)).setScaleSize(IconSizeManager.ICON_SIZE).getBitmap(mContext);
            }
        }

        mTimer.schedule(new TimerTask() {   //匿名内部类 , 匿名实例对象,呵呵
            @Override
            public void run() {
                if (mGameState != 2) {
                    moveLand();
                }
                if (mGameState != 2 && mIsStarting && isBirdOverGround()) {
                    gameOver();
                    return;
                }

                if(mGameState != 2 && isImpact()) {
//                    gameOver();
                }

                if (mGameState == 1) {
                    for (int i = 0; i < mPillars.size(); i++) {
                        mPillars.get(i).movePill(GAME_SPEED);
                    }

                    if (mPillars.get(0).getLoc_X() + IconSizeManager.PILLAR_WIDTH < 0) {
                        mPillars.remove(0);
                    }

                    if (ScreenManager.SCREEN_WIDTH - (mPillars.get(mPillars.size() - 1).getLoc_X() + IconSizeManager.PILLAR_WIDTH) > IconSizeManager.PILLAR_MARGINS) {
                        mPillars.add(new Pillar(mContext));
                    }
                }

                if (!mPillars.get(0).isPassed() && IconSizeManager.BIRD_LOC_X > mPillars.get(0).getLoc_X()) {
                    mGameScore++;
                    mPillars.get(0).setPassed(true);
                }
                mHandler.sendEmptyMessage(0x444);
            }
        }, 0, GAME_SPEED);

        if (mBitmapLand == null) { //加null就是为了节省内存，没有必要每次都new一个实例对象，那不是有病吗
            mBitmapLand = BitmapChanger.changeSize(mIconReader.getIcon("land").getBitmap(mContext), IconSizeManager.GROUND_WIDTH, IconSizeManager.GROUND_HEIGHT);
        }
        if (mBitmapReady == null) {
            mBitmapReady = mIconReader.getIcon("text_ready").setScaleSize(IconSizeManager.ICON_SIZE).getBitmap(mContext);
        }

        if (mBitmapTutorial == null) {
            mBitmapTutorial = mIconReader.getIcon("tutorial").setScaleSize(IconSizeManager.ICON_SIZE).getBitmap(mContext);
        }

        if (mBitmapGameOver == null) {
            mBitmapGameOver = mIconReader.getIcon("text_game_over").setScaleSize(IconSizeManager.ICON_SIZE).getBitmap(mContext);
        }
        mAlpha = 255;
        Log.v(TAG, "GameView init() is over");
        Log.v(TAG, "mBitmapLand.width = " + mBitmapLand.getWidth());
        Log.v(TAG, "mBitmapLand.height = " + mBitmapLand.getHeight());
    }


    //碰撞检测，算法不明白啊,坑啊坑，用于判断与障碍物的碰撞
    private boolean isImpact() {
        if ((mPillars.get(0).getLoc_X() <= IconSizeManager.BIRD_LOC_X+IconSizeManager.BIRD_REAL_WIDTH+IconSizeManager.BIRD_WIDTH_SPACE && mPillars.get(0).getLoc_X()+IconSizeManager.PILLAR_WIDTH > IconSizeManager.BIRD_LOC_X+IconSizeManager.BIRD_REAL_WIDTH+IconSizeManager.BIRD_WIDTH_SPACE)
                || (mPillars.get(0).getLoc_X() < IconSizeManager.BIRD_LOC_X+IconSizeManager.BIRD_WIDTH_SPACE && mPillars.get(0).getLoc_X()+IconSizeManager.PILLAR_WIDTH >= IconSizeManager.BIRD_LOC_X+IconSizeManager.BIRD_WIDTH_SPACE)) {
            if (mBird.getBirdHeight()+IconSizeManager.BIRD_HEIGHT_SPACE <= mPillars.get(0).getLoc_Y()+IconSizeManager.PILLAR_HEIGHT
                    || mBird.getBirdHeight()+IconSizeManager.BIRD_HEIGHT_SPACE+IconSizeManager.BIRD_REAL_HEIGHT >= mPillars.get(0).getLoc_Y()+IconSizeManager.PILLAR_HEIGHT+IconSizeManager.PILLAR_GAP_HEIGHT) {
                return true;
            }
        }
        return false;
    }

    private boolean isBirdOverGround() {
        if (mBird.getBirdHeight() > (ScreenManager.SCREEN_HEIGHT - IconSizeManager.GROUND_HEIGHT)) {
            return true;
        }
        return false;
    }

    private void moveLand() {
        //这里的移动速度和水管相同,所以把水管的速度也拖过来计算，妈啊，复杂到完全不懂
        mLand_loc_x -= (int) (IconSizeManager.PILLAR_SPEED * GAME_SPEED * 1.0 / 1000);
    }

    private void gameOver() {
        mGameState = 2;
        mIsStarting = false;
        GameView.this.setClickable(false);
        mBird.die();
        MainActivity mainActivity = (MainActivity) mContext;
        Message message = new Message();
        message.what = 0x111;
        Bundle bundle = new Bundle();
        bundle.putInt("score", mGameScore);
        message.setData(bundle);
        mainActivity.getHandler().sendMessageDelayed(message, 1500);
        mNeedShowOver = true;
    }

    public Timer getTimer() {
        return mTimer;
    }

}
