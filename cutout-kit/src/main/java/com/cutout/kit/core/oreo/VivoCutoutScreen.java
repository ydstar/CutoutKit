package com.cutout.kit.core.oreo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
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

    @Override
    public boolean hasCutout(Activity activity) {
        boolean value = false;
        int mask = 0x00000020;
        try {
            Class<?> cls = Class.forName("android.util.FtFeature");
            Method hideMethod = cls.getMethod("isFtFeatureSupport", int.class);
            Object object = cls.newInstance();
            value = (boolean) hideMethod.invoke(object, mask);
        } catch (Exception e) {
            Log.e("tag", "get error() ", e);
        }
        return value;
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
                view.setPadding(0,0,0,0);
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
