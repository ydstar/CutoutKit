package com.cutout.kit.core.pie;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.DisplayCutout;
import android.view.View;
import android.view.WindowInsets;

import com.cutout.kit.core.CutoutScreen;
import com.cutout.kit.utils.ScreenUtil;

import java.util.List;

/**
 * Author: 侯亚东
 * Date: 2021-10-21 15:56
 * Email: hydznsqk@163.com
 * Des: 9.0的刘海屏
 */
@TargetApi(Build.VERSION_CODES.P)
public class AndroidPCutoutScreen implements CutoutScreen {

    @Override
    public boolean hasCutout(Activity activity) {
        //Android P 没有单独的判断方法，根据getCutoutRect方法的返回结果处理即可
        //这里默认返回true
        return true;
    }

    @Override
    public void setDisplayInCutout(final Activity activity) {
//        Window window = activity.getWindow();
//        // 延伸显示区域到耳朵区
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
//        lp.systemUiVisibility =  View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//        window.setAttributes(lp);
//        // 允许内容绘制到耳朵区
//        final View decorView = window.getDecorView();
//        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

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
    public void getCutoutRect(Activity activity, final CutoutSizeCallback callback) {
        final View contentView = activity.getWindow().getDecorView();
        contentView.post(new Runnable() {
            @Override
            public void run() {
                WindowInsets windowInsets = contentView.getRootWindowInsets();
                if (windowInsets != null) {
                    DisplayCutout cutout = windowInsets.getDisplayCutout();
                    if (cutout != null) {
                        List<Rect> rectList = cutout.getBoundingRects();
                        callback.onResult(rectList);
                        return;
                    }
                }
                callback.onResult(null);
            }
        });
    }
}
