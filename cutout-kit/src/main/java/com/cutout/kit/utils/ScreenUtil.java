package com.cutout.kit.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Author: 侯亚东
 * Date: 2021-10-22 16:21
 * Email: hydznsqk@163.com
 * Des: 刘海屏相关屏幕工具类
 */
public class ScreenUtil {


    /**
     * 是否是竖屏
     *
     * @param context context
     * @return 是否是竖屏
     */
    public static boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 获取根布局 FrameLayout
     *
     * @param activity activity
     * @return 根布局FrameLayout
     */
    public static View getContentView(Activity activity) {
        return activity.getWindow().getDecorView().findViewById(android.R.id.content);
    }

    /**
     * 获取页面的根部的容器ViewGroup
     *
     * @param activity activity
     * @return 页面根布局
     */
    public static View getContentFirstChild(Activity activity) {
        View contentView = getContentView(activity);
        if (contentView != null) {
            if (contentView instanceof ViewGroup) {
                return ((ViewGroup) contentView).getChildAt(0);
            }
        }
        return null;
    }

    /**
     * 获取contentView的高度，建议在onWindowFocusChanged之后调用，否则高度为0
     *
     * @param activity activity
     */
    public static int getContentViewHeight(Activity activity) {
        return getContentView(activity).getHeight();
    }

    /**
     * 获取contentView的在屏幕上的展示区域，建议在onWindowFocusChanged之后调用
     *
     * @param activity activity
     */
    public static Rect getContentViewDisplayFrame(Activity activity) {
        View contentView = getContentView(activity);
        Rect displayFrame = new Rect();
        contentView.getWindowVisibleDisplayFrame(displayFrame);
        return displayFrame;
    }

    /**
     * 获取屏幕宽高
     *
     * @param context context
     */
    public static int[] getScreenSize(Context context) {
        int[] size = new int[2];
        WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        if (Build.VERSION.SDK_INT >= 17) {
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                widthPixels = realSize.x;
                heightPixels = realSize.y;
            } catch (Throwable ignored) {
            }
        }
        size[0] = widthPixels;
        size[1] = heightPixels;
        return size;
    }

    /**
     * 通过宽高计算刘海屏的Rect
     *
     * @param cutoutWidth  刘海的宽
     * @param cutoutHeight 刘海的高
     */
    public static Rect calculateCutoutRect(Context context, int cutoutWidth, int cutoutHeight) {
        int[] screenSize = getScreenSize(context);
        int screenWidth = screenSize[0];
        int screenHeight = screenSize[1];
        int left;
        int top;
        int right;
        int bottom;
        if (isPortrait(context)) {
            left = (screenWidth - cutoutWidth) / 2;
            top = 0;
            right = left + cutoutWidth;
            bottom = cutoutHeight;
        } else {
            left = 0;
            top = (screenHeight - cutoutWidth) / 2;
            right = cutoutHeight;
            bottom = top + cutoutWidth;
        }
        return new Rect(left, top, right, bottom);
    }

    /**
     * 获取navigationBar的高度
     *
     * @param context context
     * @return navigationBar高度
     */
    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        try {
            int resourceId = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                int sizeOne = context.getResources().getDimensionPixelSize(resourceId);
                int sizeTwo = Resources.getSystem().getDimensionPixelSize(resourceId);

                if (sizeTwo >= sizeOne) {
                    return sizeTwo;
                } else {
                    float densityOne = context.getResources().getDisplayMetrics().density;
                    float densityTwo = Resources.getSystem().getDisplayMetrics().density;
                    float f = sizeOne * densityTwo / densityOne;
                    return (int) ((f >= 0) ? (f + 0.5f) : (f - 0.5f));
                }
            }
        } catch (Resources.NotFoundException ignored) {
            return 0;
        }
        return result;
    }

}
