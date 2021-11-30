package com.cutout.kit.core;

import android.app.Activity;
import android.graphics.Rect;

import java.util.List;

/**
 * Author: 侯亚东
 * Date: 2021-10-25 17:19
 * Email: hydznsqk@163.com
 * Des: 刘海屏顶层接口
 */
public interface CutoutScreen {

    /**
     * 是否是刘海屏
     *
     * @param activity activity
     * @return 是否是刘海屏
     */
    boolean hasCutout(Activity activity);

    /**
     * 设置显示到刘海区域
     *
     * @param activity activity
     */
    void setDisplayInCutout(Activity activity);

    /**
     * 得到刘海屏的区域矩形,并通过接口返出去
     *
     * @param activity activity
     * @param callback callback
     */
    void getCutoutRect(Activity activity, CutoutSizeCallback callback);

    interface CutoutSizeCallback {
        void onResult(List<Rect> rectList);
    }

}
