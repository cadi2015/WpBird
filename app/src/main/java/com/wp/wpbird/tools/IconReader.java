package com.wp.wpbird.tools;

import android.content.Context;
import android.util.Log;

import com.wp.wpbird.classes.Icon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by wp on 2015/11/22.
 */
public class IconReader {
    public static final int PICTURE_WIDTH = 1024;
    public static final int PICTURE_HEIGHT = 1024;

    private Context mContext;
    private InputStream mInputStream; //得到一个输入字节流引用对象
    private static final String TAG = "IconReader";
    private BufferedReader mBufferedReader;
    public IconReader(Context context) {
        super();
        mContext = context;
        try {
            mInputStream = mContext.getAssets().open("atlas.txt"); //getAssets方法得到一个AssetsManager实例对象，再调用open方法返回一个InputStream实例对象

        } catch (IOException e){
            e.printStackTrace();
            Log.d(TAG, TAG + "atlas.txt");
        }
    }


    public Icon getIcon(String imageName) {
        Icon icon = new Icon();
        InputStreamReader inputStreamReader = null; //一个将输入字节流转换为输入字符流的引用对象
        inputStreamReader = new InputStreamReader(mInputStream);
        Log.d(TAG, "mInputStream = " + mInputStream);
        mBufferedReader = new BufferedReader(inputStreamReader); //再将输入字符流转换为BufferdReader实例对象，这个是用来将输入字符流都放在内存中读取用的
        String line = null; //读取一行字符串，指遇到换行符算做一行哦
        try {
            while ((line = mBufferedReader.readLine() ) != null) {
                String[] eachWord = line.split(" "); //split会返回一个字符串数组对象
                Log.d(TAG, "eachWord[0] = " + eachWord[0]);
                if (eachWord[0].equals(imageName)) { //找到匹配的字符串，都放到Icon对象里，牛逼啊
                    icon.setIconName(imageName);
                    icon.setWidth(Integer.valueOf(eachWord[1]));
                    icon.setHeight(Integer.valueOf(eachWord[2]));
                    icon.setStartX(Double.valueOf(eachWord[3]));
                    icon.setStartY(Double.valueOf(eachWord[4]));
                    icon.setEndX(Double.valueOf(eachWord[5]));
                    icon.setEndY(Double.valueOf(eachWord[6]));
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mInputStream = mContext.getAssets().open("atlas.txt");  //再得到一个新输入字节流实例对象，目的就是为了每次都可以从头读取字符，有点道理哈
        } catch (IOException e) {
            e.printStackTrace();
        }
        return icon;
    }
}
