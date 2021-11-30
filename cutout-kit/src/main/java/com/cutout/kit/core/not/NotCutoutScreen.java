package com.cutout.kit.core.not;

import android.app.Activity;
import android.view.View;

import com.cutout.kit.core.CutoutScreen;
import com.cutout.kit.utils.ScreenUtil;


/**
 * Author: 侯亚东
 * Date: 2021-10-21 15:56
 * Email: hydznsqk@163.com
 * Des: 非刘海屏
 */
public class NotCutoutScreen implements CutoutScreen {

    @Override
    public boolean hasCutout(Activity activity) {
        return false;
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
        callback.onResult(null);
    }
}
