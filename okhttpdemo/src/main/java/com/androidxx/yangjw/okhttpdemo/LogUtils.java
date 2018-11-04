package com.androidxx.yangjw.okhttpdemo;

import android.util.Log;

/**
 * 创建日期：2018/11/4 on 下午9:40
 * 描述:
 * 作者:yangliang
 */
public class LogUtils {

    private static final String TAG = "OkHttp";
    private static boolean debug = true;

    public static void e(String tag, String message) {
        if (debug) {
            Log.e(TAG, tag + message);
        }
    }
}
