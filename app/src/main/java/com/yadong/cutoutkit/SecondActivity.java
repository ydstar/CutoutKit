package com.yadong.cutoutkit;

import android.os.Bundle;

import com.cutout.kit.immersionbar.ImmersionBar;

public class SecondActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

    }

    @Override
    protected void setCutoutScreen(boolean isFullScreen) {
        super.setCutoutScreen(true);
    }

    @Override
    protected void setStatusBar() {
        ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarDarkFont(false)
                .init();
    }
}