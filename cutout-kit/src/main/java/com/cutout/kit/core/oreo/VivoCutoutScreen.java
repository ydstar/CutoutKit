package com.cutout.kit.core.oreo;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;

import com.cutout.kit.core.CutoutScreen;
import com.cutout.kit.utils.ScreenUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Author: 侯亚东
 * Date: 2021-10-21 17:31
 * Email: hydznsqk@163.com
 * Des: VIVO刘海屏
 */
@TargetApi(Build.VERSION_CODES.O)
public class VivoCutoutScreen implements CutoutScreen {

    /**
     * VIVO刘海
     * The constant NOTCH_VIVO.
     */
    private static final String NOTCH_VIVO = "android.util.FtFeature";

    @Override
    public boolean hasCutout(Activity activity) {
        boolean result = false;
        int mask = 0x00000020;
        try {
            ClassLoader classLoader = activity.getClassLoader();
            @SuppressLint("PrivateApi")
            Class<?> aClass = classLoader.loadClass(NOTCH_VIVO);
            Method method = aClass.getMethod("isFeatureSupport", int.class);
            result = (boolean) method.invoke(aClass, mask);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void setDisplayInCutout(final Activity activity) {
        final View contentView = activity.getWindow().getDecorView();
        contentView.post(new Runnable() {
            @Override
            public void run() {
                View view = ScreenUtil.getContentFirstChild(activity);
                if (view == null) {
                    return;
                }
                view.setPadding(0, 0, 0, 0);
            }
        });
    }

    @Override
    public void getCutoutRect(Activity activity, CutoutSizeCallback callback) {
        ArrayList<Rect> rectList = new ArrayList<>();
        Rect rect = ScreenUtil.calculateCutoutRect(activity, getNotchWidth(activity), getNotchHeight(activity));
        rectList.add(rect);
        callback.onResult(rectList);
    }

    /**
     * vivo的适配文档中就告诉是27dp，未告知如何动态获取
     */
    public int getNotchHeight(Context context) {
        float density = getDensity(context);
        return (int) (27 * density);
    }

    /**
     * vivo的适配文档中就告诉是100dp，未告知如何动态获取
     */
    public int getNotchWidth(Context context) {
        float density = getDensity(context);
        return (int) (100 * density);
    }

    private float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }
}
