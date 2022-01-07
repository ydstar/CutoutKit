package com.cutout.kit.core.oreo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;

import com.cutout.kit.core.CutoutScreen;
import com.cutout.kit.utils.ScreenUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Author: 侯亚东
 * Date: 2021-10-21 17:30
 * Email: hydznsqk@163.com
 * Des: 华为刘海屏
 */
@TargetApi(Build.VERSION_CODES.O)
public class HuaWeiCutoutScreen implements CutoutScreen {

    /**
     * 华为刘海
     * The constant NOTCH_HUA_WEI.
     */
    private static final String NOTCH_HUA_WEI = "com.huawei.android.util.HwNotchSizeUtil";

    @Override
    public boolean hasCutout(Activity activity) {
        boolean result = false;
        try {
            ClassLoader classLoader = activity.getClassLoader();
            Class<?> aClass = classLoader.loadClass(NOTCH_HUA_WEI);
            Method get = aClass.getMethod("hasNotchInScreen");
            result = (boolean) get.invoke(aClass);
        } catch (Exception ex) {
            ex.printStackTrace();
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
        try {
            ClassLoader classLoader = activity.getClassLoader();
            Class<?> aClass = classLoader.loadClass(NOTCH_HUA_WEI);
            Method get = aClass.getMethod("getNotchSize");
            int[] ret = (int[]) get.invoke(aClass);
            if (ret != null) {
                ArrayList<Rect> rectList = new ArrayList<>();
                rectList.add(ScreenUtil.calculateCutoutRect(activity, ret[0], ret[1]));
                callback.onResult(rectList);
            } else {
                callback.onResult(null);
            }
        } catch (Throwable exception) {
            callback.onResult(null);
        }
    }
}

