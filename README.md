### 0.运行界面
![运行界面](https://img-blog.csdn.net/20180330172237128?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3N1NzQ5NTIw/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

源码下载

https://github.com/sufadi/AccessibilityServiceMonitor


### 1.需求点赞界面，进行自动点击
注意:要clickable事件为ture和有id的为主，具体箭头如下

![关注界面的点赞](https://img-blog.csdn.net/20180330164848366?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3N1NzQ5NTIw/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

![热点界面的点赞](https://img-blog.csdn.net/20180330164902173?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3N1NzQ5NTIw/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

![好友界面的点赞](https://img-blog.csdn.net/20180330164915326?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3N1NzQ5NTIw/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

### 2.点击事件处理
事件输入

```
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        String packageName = event.getPackageName().toString();
        String className = event.getClassName().toString();
        // Log.d(TAG,"event = " + event.toString() );

        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                if ("com.gotokeep.keep".equals(packageName) && ("android.support.v7.widget.RecyclerView".equals(className))) {
                    // 关注界面的点赞
                    keepAppPraise("com.gotokeep.keep:id/item_cell_praise_container");

                    // 好友界面的点赞
                    keepAppPraise("com.gotokeep.keep:id/stroke_view");

                    // 热点界面的点赞
                    keepAppPraise("com.gotokeep.keep:id/layout_like");
                }
                break;

        }
    }
```
处理点击事件

```
    private void keepAppPraise(String id) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();

        if (nodeInfo != null) {
            // 该界面下所有 ViewId 节点
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(id);
            for (AccessibilityNodeInfo item : list) {
                if (item.isClickable()) {
                    Log.d(TAG, "keepAppPraise = " + item.getClassName());
                    item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }

    }
```

### 3.其他辅助代码
#### 3.1 判断权限有无

```
    public static boolean isAccessibilitySettingsOn(Context mContext, String className) {
        int accessibilityEnabled = 0;
        final String service = mContext.getPackageName() + "/" + className;
        Log.i(TAG, "service:" + service);
        // formate: service:com.fadi.forestautoget/com.fadi.forestautoget.service.AccessibilityServiceMonitor
        try {
            accessibilityEnabled = Settings.Secure.getInt(mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Error finding setting, default accessibility to not found: " + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            Log.v(TAG, "***ACCESSIBILITY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            // com.z.buildingaccessibilityservices/com.z.buildingaccessibilityservices.TestService
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();

                    Log.v(TAG, "-------------- > accessibilityService :: " + accessibilityService + " " + service);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Log.v(TAG, "***ACCESSIBILITY IS DISABLED***");
        }
        return false;
    }
```
#### 3.2 跳转辅助设置界面

```
    public static void showSettingsUI(Context mContext) {
        Intent mIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        mContext.startActivity(mIntent);
    }
```

#### 3.3 配置检测参数
onServiceConnected 事件检测 或者 accessibility_config.xml 配置
```
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo serviceInfo = new AccessibilityServiceInfo();
        serviceInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        serviceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        serviceInfo.packageNames = new String[]{"com.gotokeep.keep"};// 监控的app
        serviceInfo.notificationTimeout = 100;
        setServiceInfo(serviceInfo);
    }

```
#### 3.3 AndroidManifest.xml

```
        <service
            android:name=".service.AccessibilityServiceMonitor"
            android:enabled="true"
            android:exported="true"
            android:label="@string/app_name"
            android:permission="android.permission.BIND_ACCESSIBILITY_SERVICE">

            <intent-filter>
                <action android:name="android.accessibilityservice.AccessibilityService" />
            </intent-filter>

            <meta-data
                android:name="android.accessibilityservice"
                android:resource="@xml/accessibility_config">

            </meta-data>
```



