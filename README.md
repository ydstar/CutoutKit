# CutoutKit
沉浸式状态栏+刘海屏适配组件


## 导入方式

仅支持`AndroidX`
```
dependencies {
     implementation 'com.github.ydstar:CutoutKit:1.0.0'
}
```

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```


## 使用方法
#### 在基类`BaseActivity`中添加如下方法
```java
public class BaseActivity extends AppCompatActivity {

    private CutoutScreenManager mInstance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置状态栏
        setStatusBar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //设置刘海屏适配
        setCutoutScreen(false);
    }

    /**
     * 设置刘海屏,默认适配刘海屏和普通屏幕(支持页面重写该方法)
     * @param isFullScreen 是否全屏展示(页面填充到状态栏区域) true:是  false:不是
     */
    protected void setCutoutScreen(boolean isFullScreen) {
        if (mInstance == null) {
            mInstance = CutoutScreenManager.getInstance();
            mInstance.init(this, isFullScreen);
        }
    }

    /**
     * 设置状态栏,默认白底黑字(支持页面重写该方法)
     */
    protected void setStatusBar() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.white)//状态栏底部颜色为白色
                .statusBarDarkFont(true)//状态栏字体为黑色
                .init();
    }
}
```

#### 根据自己的场景不同选择不同的方案

#### 场景1.默认情况下,状态栏为白底黑字,不需要重写上面两个方法,正常写代码就好

#### 场景2.页面全屏,页面填充到状态栏的位置,状态栏为白底黑字,只需要重写如下方法
```java
    @Override
    protected void setCutoutScreen(boolean isFullScreen) {
        //设置为true是让页面充满全屏(填充到状态栏)
        super.setCutoutScreen(true);
    }
```

#### 场景3.页面全屏,页面填充到状态栏的位置,状态栏全透明,状态栏字体为黑色,重写如下方法
```java
    @Override
    protected void setCutoutScreen(boolean isFullScreen) {
        //设置为true是让页面充满全屏(填充到状态栏)
        super.setCutoutScreen(true);
    }

    @Override
    protected void setStatusBar() {
        ImmersionBar.with(this)
                .transparentStatusBar()//状态栏透明
                .statusBarDarkFont(true)//状态栏字体为黑色
                .init();
    }
```

#### 场景4.1Activity页面全屏,页面填充到状态栏的位置,但是需要设置标题栏顶部padding到刘海或状态栏位置
#### 场景4.2Activity页面全屏,包裹fragment,fragment需要设置顶部标题栏到状态栏位置,同上设置fragment标题栏顶部padding就可以
```java
    @Override
    protected void setCutoutScreen(boolean isFullScreen) {
        //设置为true是让页面充满全屏(填充到状态栏)
        super.setCutoutScreen(true);
    }

    @Override
    protected void setStatusBar() {
        ImmersionBar.with(this)
                .transparentStatusBar()//状态栏透明
                .statusBarDarkFont(true)//状态栏字体为黑色
                .init();
    }

    private void setScreen() {
        //获取刘海或者状态栏的高度
        int statusBarHeight = ScreenUtil.getStatusBarHeight(this);
        //包裹顶部标题栏的外容器
        mLinearLayout.setPadding(0, statusBarHeight, 0, 0);
        mLinearLayout.setBackgroundColor(Color.WHITE);
    }
```

#### 场景5.修改状态栏的颜色,重写如下方法
```java
    @Override
    protected void setStatusBar() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.orange)//状态栏底部颜色为橙色
                .statusBarDarkFont(false)//状态栏字体为白色
                .init();
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



