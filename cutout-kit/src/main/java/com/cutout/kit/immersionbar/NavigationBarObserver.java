package com.cutout.kit.immersionbar;

import static com.cutout.kit.immersionbar.Constants.IMMERSION_EMUI_NAVIGATION_BAR_HIDE_SHOW;
import static com.cutout.kit.immersionbar.Constants.IMMERSION_MIUI_NAVIGATION_BAR_HIDE_SHOW;

import android.app.Application;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;

import java.util.ArrayList;


/**
 * Author: 侯亚东
 * Date: 2021-10-21 17:02
 * Email: hydznsqk@163.com
 * Des: 导航栏显示隐藏处理，目前只支持emui和miui带有导航栏的手机
 */
final class NavigationBarObserver extends ContentObserver {

    private ArrayList<OnNavigationBarListener> mListeners;
    private Application mApplication;
    private Boolean mIsRegister = false;

    static NavigationBarObserver getInstance() {
        return NavigationBarObserverInstance.INSTANCE;
    }

    private NavigationBarObserver() {
        super(new Handler(Looper.getMainLooper()));
    }

    void register(Application application) {
        this.mApplication = application;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mApplication != null
                && mApplication.getContentResolver() != null && !mIsRegister) {
            Uri uri = null;
            if (OSUtils.isMIUI()) {
                uri = Settings.Global.getUriFor(IMMERSION_MIUI_NAVIGATION_BAR_HIDE_SHOW);
            } else if (OSUtils.isEMUI()) {
                if (OSUtils.isEMUI3_x() || Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    uri = Settings.System.getUriFor(IMMERSION_EMUI_NAVIGATION_BAR_HIDE_SHOW);
                } else {
                    uri = Settings.Global.getUriFor(IMMERSION_EMUI_NAVIGATION_BAR_HIDE_SHOW);
                }
            }
            if (uri != null) {
                mApplication.getContentResolver().registerContentObserver(uri, true, this);
                mIsRegister = true;
            }
        }
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mApplication != null && mApplication.getContentResolver() != null
                && mListeners != null && !mListeners.isEmpty()) {
            int show = 0;
            if (OSUtils.isMIUI()) {
                show = Settings.Global.getInt(mApplication.getContentResolver(), IMMERSION_MIUI_NAVIGATION_BAR_HIDE_SHOW, 0);
            } else if (OSUtils.isEMUI()) {
                if (OSUtils.isEMUI3_x() || Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    show = Settings.System.getInt(mApplication.getContentResolver(), IMMERSION_EMUI_NAVIGATION_BAR_HIDE_SHOW, 0);
                } else {
                    show = Settings.Global.getInt(mApplication.getContentResolver(), IMMERSION_EMUI_NAVIGATION_BAR_HIDE_SHOW, 0);
                }
            }
            for (OnNavigationBarListener onNavigationBarListener : mListeners) {
                onNavigationBarListener.onNavigationBarChange(show != 1);
            }
        }
    }

    void addOnNavigationBarListener(OnNavigationBarListener listener) {
        if (listener == null) {
            return;
        }
        if (mListeners == null) {
            mListeners = new ArrayList<>();
        }
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }

    void removeOnNavigationBarListener(OnNavigationBarListener listener) {
        if (listener == null || mListeners == null) {
            return;
        }
        mListeners.remove(listener);
    }

    private static class NavigationBarObserverInstance {
        private static final NavigationBarObserver INSTANCE = new NavigationBarObserver();
    }

}
