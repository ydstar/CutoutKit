package com.yadong.cutoutkit;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cutout.kit.CutoutScreenManager;
import com.cutout.kit.immersionbar.ImmersionBar;

/**
 * Author: 侯亚东
 * Date: 2021-11-30 15:36
 * Email: hydznsqk@163.com
 * Des:
 */
public class BaseActivity extends AppCompatActivity {

    private CutoutScreenManager mInstance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setCutoutScreen(false);
    }

    /**
     * 设置状态栏,默认白底黑字(子类可以重写该方法)
     */
    protected void setStatusBar() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.white)//状态栏底部颜色为白色
                .statusBarDarkFont(true)//状态栏字体为黑色
                .init();
    }

    /**
     * 设置刘海屏,默认适配刘海屏和普通屏幕(子类可以重写该方法)
     *
     * @param isFullScreen 是否全屏展示(页面填充到状态栏区域) true:是  false:不是
     *                     可以参考如下链接
     */
    protected void setCutoutScreen(boolean isFullScreen) {
        if (mInstance == null) {
            mInstance = CutoutScreenManager.getInstance();
            mInstance.init(this, isFullScreen);
        }
    }


}
