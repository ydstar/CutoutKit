package com.cutout.kit;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import com.cutout.kit.core.CutoutScreen;
import com.cutout.kit.core.CutoutScreenCallback;
import com.cutout.kit.core.not.NotCutoutScreen;
import com.cutout.kit.core.oreo.HuaWeiCutoutScreen;
import com.cutout.kit.core.oreo.OppoCutoutScreen;
import com.cutout.kit.core.oreo.VivoCutoutScreen;
import com.cutout.kit.core.oreo.XiaoMiCutoutScreen;
import com.cutout.kit.core.pie.AndroidPCutoutScreen;
import com.cutout.kit.model.CutoutScreenInfo;
import com.cutout.kit.utils.RomUtils;
import com.cutout.kit.utils.ScreenUtil;

import java.util.List;

/**
 * Author: 侯亚东
 * Date: 2021-10-21 17:02
 * Email: hydznsqk@163.com
 * Des: 刘海屏管理者
 */
public class CutoutScreenManager {

    private final CutoutScreen mCutoutScreen;
    private int mStatusBarHeight;
    private boolean mIsInit = false;

    private CutoutScreenManager() {
        mCutoutScreen = getCutoutScreen();
    }

    public static CutoutScreenManager getInstance() {
        return SingleHolder.INSTANCE;
    }

    private static class SingleHolder {
        public static final CutoutScreenManager INSTANCE = new CutoutScreenManager();
    }

    /**
     * 设置适配刘海屏和非刘海屏
     *
     * @param activity     activity
     * @param isFullScreen 是否全屏展示(页面顶到状态栏区域) true:是  false:不是
     */
    public void init(final Activity activity, final boolean isFullScreen) {
        //1.支持显示到刘海区域
        setDisplayInCutout(activity);
        //2.获取刘海屏信息
        getCutoutScreenInfo(activity, new CutoutScreenCallback() {
            @Override
            public void onResult(CutoutScreenInfo info) {
                mIsInit = true;
//                Toast.makeText(activity, "是否是刘海屏手机 " + info.hasCutout, Toast.LENGTH_SHORT).show();
                if (info.hasCutout) {
                    //刘海屏,不设置setFitsSystemWindows
                    handleCutoutScreen(info, activity, isFullScreen);
                } else {//普通屏幕设置
                    handleDefaultScreen(activity, isFullScreen);
                }
            }
        });
    }

    /**
     * 设置显示到刘海区域
     *
     * @param activity 当前的activity页面
     */
    private void setDisplayInCutout(Activity activity) {
        if (mCutoutScreen != null) {
            mCutoutScreen.setDisplayInCutout(activity);
        }
    }

    /**
     * 处理刘海屏
     */
    private void handleCutoutScreen(final CutoutScreenInfo info, Activity activity, boolean isFullScreen) {
        final View view = ScreenUtil.getContentFirstChild(activity);
        for (final Rect rect : info.cutoutRectList) {
            if (rect.top == 0) {
                mStatusBarHeight = rect.bottom;
                if (isFullScreen) {
                    return;
                }
                if (view == null) {
                    return;
                }
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                layoutParams.topMargin = rect.bottom;
                view.setLayoutParams(layoutParams);
                return;
            }
        }
    }

    /**
     * 处理普通屏幕
     */
    private void handleDefaultScreen(Activity activity, boolean isFullScreen) {
        int statusBarHeight = ScreenUtil.getStatusBarHeight(activity);
        mStatusBarHeight = statusBarHeight;
        if (isFullScreen) {
            return;
        }
        View view = ScreenUtil.getContentFirstChild(activity);
        if (view == null) {
            return;
        }
        view.setPadding(0, statusBarHeight, 0, 0);
    }

    /**
     * 获取刘海屏信息
     *
     * @param activity 当前的activity页面
     * @param listener 回调
     */
    private void getCutoutScreenInfo(Activity activity, final CutoutScreenCallback listener) {
        final CutoutScreenInfo info = new CutoutScreenInfo();
        if (mCutoutScreen.hasCutout(activity)) {
            mCutoutScreen.getCutoutRect(activity, new CutoutScreen.CutoutSizeCallback() {
                @Override
                public void onResult(List<Rect> rectList) {
                    if (rectList != null && rectList.size() > 0) {
                        info.hasCutout = true;
                        info.cutoutRectList = rectList;
                    }
                    listener.onResult(info);
                }
            });
        } else {
            final View contentView = activity.getWindow().getDecorView();
            contentView.post(new Runnable() {
                @Override
                public void run() {
                    listener.onResult(info);
                }
            });
        }
    }

    /**
     * 获取刘海或状态栏的高度
     *
     * @return int
     */
    public int getStatusBarHeight() {
        check();
        return mStatusBarHeight;
    }

    private void check() {
        if (!mIsInit) {
            throw new RuntimeException("CutoutScreenManager 的 init 还未初始化");
        }
    }

    private CutoutScreen getCutoutScreen() {
        CutoutScreen screen;
        //9.0,用系统的api
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            screen = new AndroidPCutoutScreen();
        }
        //8.0用各家厂商提供的api
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //华为
            if (RomUtils.isHuawei()) {
                screen = new HuaWeiCutoutScreen();
            }
            //小米
            else if (RomUtils.isXiaomi()) {
                screen = new XiaoMiCutoutScreen();
            }
            //oppo
            else if (RomUtils.isOppo()) {
                screen = new OppoCutoutScreen();
            }
            //vivo
            else if (RomUtils.isVivo()) {
                screen = new VivoCutoutScreen();
            } else {
                //普通的屏幕(非刘海屏)
                screen = new NotCutoutScreen();
            }
        } else {
            //普通的屏幕(非刘海屏)
            screen = new NotCutoutScreen();
        }
        return screen;
    }
}
