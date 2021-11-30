# CutoutKit
沉浸式状态栏+刘海屏适配组件

#### 在基类`BaseActivity`中有如下方法
```java
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setCutoutScreen(false);
        setStatusBar();
        super.onCreate(savedInstanceState);
    }

    /**
     * 设置刘海屏,默认适配刘海屏和普通屏幕(支持页面重写该方法)
     * @param isFullScreen 是否全屏展示(页面填充到状态栏区域) true:是  false:不是
     */
    protected void setCutoutScreen(boolean isFullScreen) {
        CutoutScreenManager instance = CutoutScreenManager.getInstance();
        instance.init(this,isFullScreen);
    }

    /**
     * 设置状态栏,默认白底黑字(支持页面重写该方法)
     */
    protected void setStatusBar() {
         //状态栏底部颜色为白色
        StatusBarUtil.setColor(this, Color.WHITE, 0);
         //状态栏字体为黑色
        StatusBarUtil.setLightMode(this);
    }
}
```

#### 根据自己的场景不同选择不同的方案

#### 场景1.默认情况下,状态栏为白底黑字,不需要重写上面两个方法,正常写代码就好

#### 场景2.页面全屏,页面填充到状态栏的位置,重写如下方法
```java
    @Override
    protected void setCutoutScreen(boolean isFullScreen) {
        //设置为true是让页面充满全屏(填充到状态栏)
        super.setCutoutScreen(true);
    }
```

#### 场景3.页面全屏,页面填充到状态栏的位置,状态栏全透明,重写如下方法
```java
    @Override
    protected void setCutoutScreen(boolean isFullScreen) {
        //设置为true是让页面充满全屏(填充到状态栏)
        super.setCutoutScreen(true);
    }
    @Override
    protected void setStatusBar() {
        //状态栏全透明
        StatusBarUtil.setTransparent(this);
         //状态栏字体为黑色
        StatusBarUtil.setLightMode(this);
    }
```

#### 场景4.页面全屏,页面填充到状态栏的位置,但是需要标题栏设置顶部padding到刘海或状态栏位置
```java
    @Override
    protected void setCutoutScreen(boolean isFullScreen) {
        //设置为true是让页面充满全屏(填充到状态栏)
        super.setCutoutScreen(true);
    }
    @Override
    protected void setStatusBar() {
        //状态栏全透明
        StatusBarUtil.setTransparent(this);
         //状态栏字体为黑色
        StatusBarUtil.setLightMode(this);
    }

    private void setScreen() {
        //获取刘海或者状态栏的高度
        int statusBarHeight = CutoutScreenManager.getInstance().getStatusBarHeight();
        mLinearLayout.setPadding(0, statusBarHeight, 0, 0);
        mLinearLayout.setBackgroundColor(Color.WHITE);
    }
```

#### 场景5.修改状态栏的颜色,重写如下方法
```java
    @Override
    protected void setStatusBar() {
        //比如修改颜色为YELLOW
        StatusBarUtil.setColor(this, Color.YELLOW, 0);
        //状态栏字体为黑色
        StatusBarUtil.setLightMode(this);
    }
```

## License
```text
Copyright [2021] [ydStar]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

