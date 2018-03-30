package com.fadi.forestautoget.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;


public class AccessibilityServiceMonitor extends AccessibilityService {


    private static final String TAG = AccessibilityServiceMonitor.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

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

    @Override
    public void onInterrupt() {

    }

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

}
