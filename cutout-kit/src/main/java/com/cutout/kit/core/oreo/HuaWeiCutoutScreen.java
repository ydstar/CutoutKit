package com.cutout.kit.core.oreo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.cutout.kit.core.CutoutScreen;
import com.cutout.kit.utils.ScreenUtil;

import java.lang.reflect.Constructor;
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
     * 刘海屏全屏显示FLAG
     */
    public static final int FLAG_CUTOUT_SUPPORT = 0x00010000;

    @Override
    public boolean hasCutout(Activity activity) {
        boolean ret = false;
        try {
            ClassLoader cl = activity.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (Throwable ignore) {
        }
        return ret;
    }

    @Override
    public void setDisplayInCutout(final Activity activity) {
        try {
            Window window = activity.getWindow();
            if (window == null) {
                return;
            }
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            Class layoutParamsExCls = Class.forName("com.huawei.android.view.LayoutParamsEx");
            Constructor con = layoutParamsExCls.getConstructor(WindowManager.LayoutParams.class);
            Object layoutParamsExObj = con.newInstance(layoutParams);
            Method method = layoutParamsExCls.getMethod("addHwFlags", int.class);
            method.invoke(layoutParamsExObj, FLAG_CUTOUT_SUPPORT);
            window.getWindowManager().updateViewLayout(window.getDecorView(), window.getDecorView().getLayoutParams());
        } catch (Throwable ignore) {
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
    }

    @Override
    public void getCutoutRect(Activity activity, CutoutSizeCallback callback) {
        try {
            ClassLoader cl = activity.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("getNotchSize");
            int[] ret = (int[]) get.invoke(HwNotchSizeUtil);
            ArrayList<Rect> rectList = new ArrayList<>();
            rectList.add(ScreenUtil.calculateCutoutRect(activity, ret[0], ret[1]));
            callback.onResult(rectList);
        } catch (Throwable ignore) {
            callback.onResult(null);
        }
    }
}

