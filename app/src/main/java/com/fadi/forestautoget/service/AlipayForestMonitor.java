package com.fadi.forestautoget.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.fadi.forestautoget.util.Config;

import java.util.List;

public class AlipayForestMonitor {


    /**
     * 启动支付宝界面
     * adb shell am start com.eg.android.AlipayGphone/com.eg.android.AlipayGphone.AlipayLogin
     */
    public static void startAlipay(Context mContext) {
        Intent intent = new Intent();
        intent.setPackage("com.eg.android.AlipayGphone");
        intent.setClassName("com.eg.android.AlipayGphone", "com.eg.android.AlipayGphone.AlipayLogin");
        mContext.startActivity(intent);
    }

    /**
     * 自动点击进入蚂蚁森林界面
     */
    public static void enterForestUI(AccessibilityNodeInfo nodeInfo) {
        Log.d(Config.TAG, "enterForestUI ");
        if (nodeInfo != null) {
            // 找到界面中蚂蚁森林的文字
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("蚂蚁森林");

            if (list == null) {
                Log.d(Config.TAG, "enterForestUI finding no");
                return;
            } else {
                Log.d(Config.TAG, "enterForestUI finding yes");
            }

            for (AccessibilityNodeInfo item : list) {
                /**
                 *  蚂蚁森林本身不可点击，但是他的父控件可以点击
                 */
                AccessibilityNodeInfo parent = item.getParent();
                if (null != parent && parent.isClickable()) {
                    parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    Log.d(Config.TAG, "item = " + item.toString() + ", parent click = " + parent.toString());
                    break;
                }
            }
        }
    }

    public static void policy(AccessibilityNodeInfo nodeInfo, String packageName, String className) {
        /**
         * 蚂蚁森林界面
         */
        if (packageName.equals("com.eg.android.AlipayGphone") &&
                ("com.alipay.mobile.nebulacore.ui.H5Activity".equals(className)
                || "com.uc.webkit.bf".equals(className))) {

            if (nodeInfo != null) {
                for (int i = 0; i < nodeInfo.getChildCount(); i++) {
                    AccessibilityNodeInfo child =  nodeInfo.getChild(i);
                    if ("com.uc.webview.export.WebView".equals(child.getClassName())) {
                        Log.d(Config.TAG, "找到蚂蚁森林的 webView count = " + child.getChildCount());

                        findEveryViewNode(child);
                        break;
                    }
                }
            } else {
                Log.d(Config.TAG, "alipayPolicy = null");
            }
        }

    }

    public static void findEveryViewNode(AccessibilityNodeInfo node) {
        if (null != node && node.getChildCount() > 0) {
            for (int i = 0; i < node.getChildCount(); i++) {
                AccessibilityNodeInfo child =  node.getChild(i);
                // 有时 child 为空
                if (child == null) {
                    continue;
                }

                //Log.d(TAG, "findEveryViewNode = " + child.toString());

                String className = child.getClassName().toString();
                if ("android.widget.Button".equals(className)) {
                    Log.d(Config.TAG, "Button 的节点数据 text = " + child.getText() + ", descript = " + child.getContentDescription() + ", className = " + child.getClassName() + ", resId = " + child.getViewIdResourceName());

                    boolean isClickable = child.isClickable();
                    boolean isResIdNull = child.getViewIdResourceName() == null ? true:false;

                    /**
                     * 好友的能量不能收取，因为支付宝在onTouch事件中return true,导致不会触发OnClick方法
                     *
                     * 但是支付宝中的蚂蚁森林可以收取自己的能量
                     */
                    if ( isClickable && isResIdNull) {
                        child.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        Log.d(Config.TAG, "能量球 成功点击");
                    }
                }

                // 递归调用
                findEveryViewNode(child);
            }
        }
    }

}
