package com.fadi.forestautoget.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.fadi.forestautoget.MyApplication;
import com.fadi.forestautoget.util.Config;
import com.fadi.forestautoget.util.ShareUtil;

import java.util.List;


public class AccessibilityServiceMonitor extends AccessibilityService {

    private static final String TAG = AccessibilityServiceMonitor.class.getSimpleName();

    public static final String ACTION_UPDATE_SWITCH = "action_update_switch";
    public static final String ACTION_ALAM_TIMER= "action_alarm_timer";

    private boolean isKeepEnable = true;
    private boolean isAlipayForest = true;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null == intent) {
            return super.onStartCommand(intent, flags, startId);
        }

        String action = intent.getAction();
        Log.d(TAG, "onStartCommand Aciton: " + action);

        if (ACTION_UPDATE_SWITCH.equals(action)) {
            updateSwitchStatus();
        } else if (ACTION_ALAM_TIMER.equals(action)) {
            MyApplication.startAlarmTask(this);
        }

        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo serviceInfo = new AccessibilityServiceInfo();
        serviceInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        serviceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        serviceInfo.packageNames = new String[]{"com.gotokeep.keep", "com.eg.android.AlipayGphone"};// 监控的app
        serviceInfo.notificationTimeout = 100;
        serviceInfo.flags = serviceInfo.flags | AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY;
        setServiceInfo(serviceInfo);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        String packageName = event.getPackageName().toString();
        String className = event.getClassName().toString();
        Log.d(Config.TAG,"packageName = " + packageName + ", className = " + className);

        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                if (isKeepEnable) {
                    KeepAppMonitor.policy(getRootInActiveWindow(), packageName, className);
                }

                if (isAlipayForest) {
                    AlipayForestMonitor.policy(getRootInActiveWindow(), packageName, className);
                }
                break;

        }
    }

    @Override
    public void onInterrupt() {

    }

    /**
     * 更新开关状态
     */
    private void updateSwitchStatus() {
        ShareUtil mShareUtil = new ShareUtil(this);
        isKeepEnable = mShareUtil.getBoolean(Config.APP_KEEP, true);
        isAlipayForest = mShareUtil.getBoolean(Config.APP_ALIPAY_FOREST, true);
    }


}
