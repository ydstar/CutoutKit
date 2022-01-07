package com.cutout.kit.core.oreo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;

import com.cutout.kit.core.CutoutScreen;
import com.cutout.kit.utils.ScreenUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Author: 侯亚东
 * Date: 2021-10-21 17:30
 * Email: hydznsqk@163.com
 * Des: OPPO刘海屏
 */
@TargetApi(Build.VERSION_CODES.O)
public class OppoCutoutScreen implements CutoutScreen {

    /**
     * OPPO刘海
     * The constant NOTCH_OPPO.
     */
    private static final String NOTCH_OPPO = "com.oppo.feature.screen.heteromorphism";

    @Override
    public boolean hasCutout(Activity activity) {
        boolean ret = false;
        try {
            ret = activity.getPackageManager().hasSystemFeature(NOTCH_OPPO);
        } catch (Throwable ignore) {
        }
        return ret;
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
            String screenValue = getScreenValue();
            if (!TextUtils.isEmpty(screenValue)) {
                String[] split = screenValue.split(":");
                String leftTopPoint = split[0];
                String[] leftAndTop = leftTopPoint.split(",");
                String rightBottomPoint = split[1];
                String[] rightAndBottom = rightBottomPoint.split(",");
                int left;
                int top;
                int right;
                int bottom;
                if (ScreenUtil.isPortrait(activity)) {
                    left = Integer.parseInt(leftAndTop[0]);
                    top = Integer.parseInt(leftAndTop[1]);
                    right = Integer.parseInt(rightAndBottom[0]);
                    bottom = Integer.parseInt(rightAndBottom[1]);
                } else {
                    left = Integer.parseInt(leftAndTop[1]);
                    top = Integer.parseInt(leftAndTop[0]);
                    right = Integer.parseInt(rightAndBottom[1]);
                    bottom = Integer.parseInt(rightAndBottom[0]);
                }
                Rect rect = new Rect(left, top, right, bottom);
                ArrayList<Rect> rectList = new ArrayList<>();
                rectList.add(rect);
                callback.onResult(rectList);
            }
        } catch (Throwable ignore) {
            callback.onResult(null);
        }
    }

    /**
     * 获取刘海的坐标
     * 属性形如：[ro.oppo.screen.heteromorphism]: [378,0:702,80]
     * 获取到的值为378,0:702,80
     * (378,0)是刘海区域左上角的坐标
     * (702,80)是刘海区域右下角的坐标
     */
    private String getScreenValue() {
        String value = "";
        Class<?> cls;
        try {
            cls = Class.forName("android.os.SystemProperties");
            Method get = cls.getMethod("get", String.class);
            Object object = cls.newInstance();
            value = (String) get.invoke(object, "ro.oppo.screen.heteromorphism");
        } catch (Throwable ignore) {
        }
        return value;
    }
}
