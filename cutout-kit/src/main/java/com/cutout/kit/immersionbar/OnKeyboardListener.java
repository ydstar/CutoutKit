package com.cutout.kit.immersionbar;


/**
 * Author: 侯亚东
 * Date: 2021-10-21 17:02
 * Email: hydznsqk@163.com
 * Des: 软键盘监听
 */
public interface OnKeyboardListener {
    /**
     * On keyboard change.
     *
     * @param isPopup        the is popup  是否弹出
     * @param keyboardHeight the keyboard height  软键盘高度
     */
    void onKeyboardChange(boolean isPopup, int keyboardHeight);
}
