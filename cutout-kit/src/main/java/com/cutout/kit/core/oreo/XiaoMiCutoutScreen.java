package com.cutout.kit.core.oreo;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.Window;

import com.cutout.kit.core.CutoutScreen;
import com.cutout.kit.utils.ScreenUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Author: 侯亚东
 * Date: 2021-10-21 17:17
 * Email: hydznsqk@163.com
 * Des: 小米刘海屏
 */
@TargetApi(Build.VERSION_CODES.O)
public class XiaoMiCutoutScreen implements CutoutScreen {

    /**
     * 系统属性
     * The constant SYSTEM_PROPERTIES.
     */
    private static final String SYSTEM_PROPERTIES = "android.os.SystemProperties";

    /**
     * 小米刘海
     * The constant NOTCH_XIAO_MI.
     */
    private static final String NOTCH_XIAO_MI = "ro.miui.notch";

    @Override
    public boolean hasCutout(Activity activity) {
        int result = 0;
        try {
            ClassLoader classLoader = activity.getClassLoader();
            @SuppressLint("PrivateApi")
            Class<?> aClass = classLoader.loadClass(SYSTEM_PROPERTIES);
            Method method = aClass.getMethod("getInt", String.class, int.class);
            result = (Integer) method.invoke(aClass, NOTCH_XIAO_MI, 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result == 1;
    }

    @Override
    public void setDisplayInCutout(final Activity activity) {
        int flag = 0x00000100 | 0x00000200 | 0x00000400;
        try {
            Method method = Window.class.getMethod("addExtraFlags", int.class);
            method.invoke(activity.getWindow(), flag);
        } catch (Exception ignore) {
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
        Rect rect = ScreenUtil.calculateCutoutRect(activity, getCutoutWidth(activity), getCutoutHeight(activity));
        ArrayList<Rect> rectList = new ArrayList<>();
        rectList.add(rect);
        callback.onResult(rectList);
    }

    /**
     * 获取刘海宽
     */
    private int getCutoutWidth(Context context) {
        int resourceId = context.getResources().getIdentifier("notch_width", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * 获取刘海高
     */
    private int getCutoutHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("notch_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
