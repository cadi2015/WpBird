package com.wp.wpbird.tools;

/**
 * Created by wp on 2015/11/30.
 */
public class IconSizeManager {

    private IconSizeManager() {
        super();
    }

    public static float ICON_SIZE;   //静态变量
    public static int GROUND_WIDTH;
    public static int GROUND_HEIGHT;
    public static int PILLAR_SPEED;
    public static int BIRD_LOC_X;
    public static int BIRD_INIT_LOC_Y;
    public static int BIRD_REAL_HEIGHT;
    public static int BIRD_TURNING_POINT_X;
    public static int BIRD_TURNING_POINT_Y;
    public static int BIRD_WIDTH;
    public static int BIRD_JUMP_HEIGHT;
    public static int PILLAR_GAP_HEIGHT;
    public static int PILLAR_MARGINS;
    public static int PILLAR_WIDTH;
    public static int PILLAR_HEIGHT;
    public static int BIRD_REAL_WIDTH;
    public static int BIRD_WIDTH_SPACE;
    public static int BIRD_HEIGHT_SPACE;
    public static int BIRD_HEIGHT;
    public static void init() {  //用静态方法初始化
        ICON_SIZE = ScreenManager.SCREEN_DENSITY * 1.3f;
        BIRD_LOC_X = (int) (ScreenManager.SCREEN_WIDTH * 1.0 / 5 );
        BIRD_TURNING_POINT_X = (int) (24 * ICON_SIZE);
        BIRD_TURNING_POINT_Y = (int) (26 * ICON_SIZE);
        BIRD_WIDTH = (int) (48 * ICON_SIZE);
        BIRD_REAL_HEIGHT = (int) (24 * ICON_SIZE);
        BIRD_REAL_WIDTH = (int) (34 * ICON_SIZE);
        BIRD_WIDTH_SPACE = (BIRD_WIDTH - BIRD_REAL_WIDTH ) / 2;
        GROUND_HEIGHT = (int) (ScreenManager.SCREEN_HEIGHT * 112.0 / 512);
        GROUND_WIDTH = 3 * GROUND_HEIGHT;
        BIRD_INIT_LOC_Y = (ScreenManager.SCREEN_HEIGHT - GROUND_HEIGHT ) / 2 + BIRD_REAL_HEIGHT;
        BIRD_JUMP_HEIGHT = (int) (BIRD_WIDTH * 1.1);
        BIRD_HEIGHT_SPACE = (BIRD_WIDTH - BIRD_REAL_HEIGHT) / 2;
        PILLAR_SPEED = ScreenManager.SCREEN_WIDTH / 2;
        PILLAR_GAP_HEIGHT = BIRD_JUMP_HEIGHT * 2;
        PILLAR_MARGINS = BIRD_WIDTH * 3;
        PILLAR_WIDTH = (int) (52 * ICON_SIZE);
        PILLAR_HEIGHT = (int) (320 * ICON_SIZE);
    }
}
