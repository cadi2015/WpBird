package com.wp.wpbird.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wp.wpbird.R;
import com.wp.wpbird.tools.BitmapChanger;
import com.wp.wpbird.tools.IconReader;
import com.wp.wpbird.tools.IconSizeManager;
import com.wp.wpbird.tools.ScoreManager;
import com.wp.wpbird.tools.ScreenManager;
import com.wp.wpbird.view.GameView;

/**
 * Created by wp on 2015/11/22.
 */
public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private ImageView mBackGround;
    private IconReader mIconReader;
    private RelativeLayout mPanel;
    private GameView mGameView;
    private Bitmap mBitmapScorePanel;
    private Bitmap[] mBitmapMedals;
    private Bitmap[] mBitmapScoreNumbers;
    private Bitmap mBitmapBtnPlay;
    private Bitmap mBitmapBtnScore;
    private Bitmap mBitmapNew;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(R.layout.activity_main);
        initView();
    }


    private void init() {
        mIconReader = new IconReader(this);
        ScreenManager.init(getWindowManager());
        IconSizeManager.init();
        ScoreManager.init(this);
        mBitmapScorePanel = mIconReader.getIcon("score_panel").setScaleSize(IconSizeManager.ICON_SIZE).getBitmap(this);
        mBitmapMedals = new Bitmap[4];
        for (int i = 0; i < mBitmapMedals.length; i++) {
            mBitmapMedals[i] = mIconReader.getIcon("medals_" + String.valueOf(i)).setScaleSize(IconSizeManager.ICON_SIZE).getBitmap(this);
        }
        mBitmapScoreNumbers = new Bitmap[10];
        for (int i = 0; i < mBitmapScoreNumbers.length; i++) {
            mBitmapScoreNumbers[i] = mIconReader.getIcon("number_score_0" + String.valueOf(i)).setScaleSize(IconSizeManager.ICON_SIZE).getBitmap(this);
        }

        mBitmapBtnPlay = mIconReader.getIcon("button_play").setScaleSize(IconSizeManager.ICON_SIZE).getBitmap(this);
        mBitmapBtnScore = mIconReader.getIcon("button_score").setScaleSize(IconSizeManager.ICON_SIZE).getBitmap(this);
        mBitmapNew = mIconReader.getIcon("new").setScaleSize(IconSizeManager.ICON_SIZE).getBitmap(this);
        if (mHandler == null) {
            mHandler = new MyMainHandler();
        }
    }

    private class MyMainHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            if (msg.what == 0x111) {
                int score = msg.getData().getInt("score");
                mGameView.getTimer().cancel();
                showPanel(MainActivity.this, score);
            }

        }
    }

    private void initView() {
        mBackGround = (ImageView) findViewById(R.id.main_iv_bg);
        mBackGround.setImageBitmap(BitmapChanger.changeSize(mIconReader.getIcon("bg_day").getBitmap(this), ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT));
        mPanel = (RelativeLayout) findViewById(R.id.main_rl_panel);
        mGameView = (GameView) findViewById(R.id.main_game_view);
    }

    private void showPanel(Context context, int score) {
        RelativeLayout score_panel = new RelativeLayout(context);
        RelativeLayout.LayoutParams score_panel_params = new RelativeLayout.LayoutParams(mBitmapScorePanel.getWidth(), mBitmapScorePanel.getHeight());
        score_panel_params.topMargin = (ScreenManager.SCREEN_HEIGHT - mBitmapScorePanel.getHeight()) / 2;
        score_panel_params.addRule(RelativeLayout.CENTER_IN_PARENT);
        score_panel.setBackgroundDrawable(new BitmapDrawable(mBitmapScorePanel));
        score_panel.setLayoutParams(score_panel_params);
        Log.d(TAG, "…………………showPanel(Context context)………………");


        ImageView medal = new ImageView(context);
        RelativeLayout.LayoutParams medal_params = new RelativeLayout.LayoutParams(mBitmapMedals[0].getWidth(), mBitmapMedals[0].getHeight());
        medal_params.topMargin = (int) (44 * IconSizeManager.ICON_SIZE);
        medal_params.leftMargin = (int) (30 * IconSizeManager.ICON_SIZE);
        medal.setLayoutParams(medal_params);
        medal.setImageBitmap(mBitmapMedals[0]);
        score_panel.addView(medal);

        int bestScore = ScoreManager.preferences.getInt("bestScore", 0);
        Log.d(TAG, "score = " + score);
        if (score > bestScore) {
            ScoreManager.setBestScore(score);
        }

        ImageView scoreNum1 = createNumber(38, 190, score % 10);
        ImageView scoreNum2 = createNumber(38, 165, score / 10);
        ImageView scoreNum3 = createNumber(80, 190, ScoreManager.preferences.getInt("bestScore", 0) % 10);
        ImageView scoreNum4 = createNumber(80, 165, ScoreManager.preferences.getInt("bestScore", 0) / 10);
        score_panel.addView(scoreNum1);
        score_panel.addView(scoreNum2);
        score_panel.addView(scoreNum3);
        score_panel.addView(scoreNum4);
//        scoreNum4.setId(4);

//        ImageView highBestNew = new ImageView(context);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mBitmapNew.getWidth(), mBitmapNew.getHeight());
//        params.addRule(RelativeLayout.LEFT_OF, scoreNum4.getId());
//        highBestNew.setLayoutParams(params);

        Button btnPlay = new Button(context);
        RelativeLayout.LayoutParams btnPlay_params = new RelativeLayout.LayoutParams(mBitmapBtnPlay.getWidth(), mBitmapBtnPlay.getHeight());
        btnPlay_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        btnPlay_params.leftMargin = (ScreenManager.SCREEN_WIDTH - mBitmapBtnPlay.getWidth()) / 8;
        btnPlay_params.bottomMargin = (ScreenManager.SCREEN_HEIGHT - mBitmapScorePanel.getHeight() - mBitmapBtnPlay.getHeight()) / 3;
        btnPlay.setBackgroundDrawable(new BitmapDrawable(mBitmapBtnPlay));
        btnPlay.setLayoutParams(btnPlay_params);

        Button btnScore = new Button(context);
        RelativeLayout.LayoutParams btnScore_params = new RelativeLayout.LayoutParams(mBitmapBtnScore.getWidth(), mBitmapBtnScore.getHeight());
        btnScore.setBackgroundDrawable(new BitmapDrawable(mBitmapBtnScore));
        btnScore_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        btnScore_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        btnScore_params.bottomMargin = (ScreenManager.SCREEN_HEIGHT - mBitmapScorePanel.getHeight() - mBitmapBtnPlay.getHeight()) / 3;
        btnScore_params.rightMargin = (ScreenManager.SCREEN_WIDTH - mBitmapBtnScore.getWidth()) / 8;
        btnScore.setLayoutParams(btnScore_params);
        mPanel.addView(score_panel);
        mPanel.addView(btnPlay);
        mPanel.addView(btnScore);
        btnPlay.setOnTouchListener(new MyBtnTouchLis(btnPlay_params, btnPlay_params.bottomMargin, btnPlay));
        btnScore.setOnTouchListener(new MyBtnTouchLis(btnScore_params, btnScore_params.bottomMargin, btnScore));
        btnPlay.setOnClickListener(new MyBtnClick());
    }

    private class MyBtnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mGameView.init();
            mPanel.removeAllViews();
            Log.d(TAG, "onClick(View v), viewId = " + v.getId());
        }

    }


    private class MyBtnTouchLis implements View.OnTouchListener {
        private RelativeLayout.LayoutParams params;
        private int btnBottom;
        private Button btn;

        public MyBtnTouchLis(RelativeLayout.LayoutParams params, int btnBottom, Button btn) {
            super();
            this.params = params;
            this.btnBottom = btnBottom;
            this.btn = btn;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.v(TAG, "onTouchEvent = " + event.getAction());
                params.bottomMargin = btnBottom - 30;
            }

            if (event.getAction() == MotionEvent.ACTION_UP) {
                params.bottomMargin = btnBottom;
            }
            btn.setLayoutParams(params);
            return false;  //这里的返回值很重要，当一个View有两个事件监听时(触摸与点击）,true代表事件完毕，不用传播，这样Click就回调不到，Android操作系统牛逼
        }
    }

    private ImageView createNumber(int topMar, int leftMar, int number) {
        ImageView scoreNum = new ImageView(this);
        RelativeLayout.LayoutParams scoreNum1_params = new RelativeLayout.LayoutParams(mBitmapScoreNumbers[0].getWidth(), mBitmapScoreNumbers[0].getHeight());
        scoreNum1_params.topMargin = (int) (topMar * IconSizeManager.ICON_SIZE);
        scoreNum1_params.leftMargin = (int) (leftMar * IconSizeManager.ICON_SIZE);
        scoreNum.setLayoutParams(scoreNum1_params);
        scoreNum.setImageBitmap(mBitmapScoreNumbers[number]);
        return scoreNum;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish(); //释放当前的Activity
            System.exit(0); //用于终结进程中的定时器,就是把整个进程干掉……
        }
        return false;
    }

    public Handler getHandler() {
        return mHandler;
    }
}
