package com.yadong.cutoutkit;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cutout.kit.immersionbar.ImmersionBar;
import com.cutout.kit.utils.ScreenUtil;
import com.navigationbar.kit.NavigationBarKit;

public class ThirdActivity extends BaseActivity {

    private NavigationBarKit mNavigationBarKit;
    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        mLinearLayout = findViewById(R.id.ll_container);
        mNavigationBarKit = findViewById(R.id.nav_bar);
        initNavigationBarKit();
        setScreen();
    }

    private void initNavigationBarKit() {
        mNavigationBarKit.setTitle("首页");
        Button leftBtn1 = mNavigationBarKit.addLeftTextButton(getString(R.string.favorite), R.integer.id_left_btn1);
        leftBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ThirdActivity.this, "左侧按钮", Toast.LENGTH_SHORT).show();
            }
        });

        Button rightBtn1 = mNavigationBarKit.addRightTextButton(getString(R.string.share), R.integer.id_right_btn1);
        rightBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ThirdActivity.this, "右侧按钮1", Toast.LENGTH_SHORT).show();
            }
        });

        Button rightBtn2 = mNavigationBarKit.addRightTextButton("分享", R.integer.id_right_btn2);
        rightBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ThirdActivity.this, "右侧按钮2", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setScreen() {
        int statusBarHeight = ScreenUtil.getStatusBarHeight(this);
        Toast.makeText(this,statusBarHeight+"",Toast.LENGTH_SHORT).show();
        mLinearLayout.setPadding(0, statusBarHeight, 0, 0);
        mLinearLayout.setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void setCutoutScreen(boolean isFullScreen) {
        super.setCutoutScreen(true);
    }

    @Override
    protected void setStatusBar() {
        ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarDarkFont(true)
                .init();
    }
}