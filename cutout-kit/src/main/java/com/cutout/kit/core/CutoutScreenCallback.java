package com.cutout.kit.core;


import com.cutout.kit.model.CutoutScreenInfo;

/**
 * Author: 侯亚东
 * Date: 2021-10-25 17:19
 * Email: hydznsqk@163.com
 * Des: 刘海屏信息回调接口
 */
public interface CutoutScreenCallback {
    void onResult(CutoutScreenInfo info);
}
