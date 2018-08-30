package com.fadi.forestautoget.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.fadi.forestautoget.util.CommonUtil;
import com.fadi.forestautoget.util.Config;

public class LiangTongMonitor {

    public static void startLiangTongUI(Context mContext) {
        Intent intent = new Intent();
        intent.setPackage("com.sinovatech.unicom.ui");
        intent.setClassName("com.sinovatech.unicom.ui", "com.sinovatech.unicom.basic.ui.MainActivity");
        mContext.startActivity(intent);
    }

    /**
     * 跳转到签到界面
     * @param nodeInfo
     * @param packageName
     * @param className
     */
    public static void startLiangTongQianDaoUI(AccessibilityNodeInfo nodeInfo, String packageName, String className) {
        if (nodeInfo == null) {
            return;
        }

        if (false == "com.sinovatech.unicom.ui".equals(packageName)) {
            return;
        }

        CommonUtil.clickBtnByResId(nodeInfo, "com.sinovatech.unicom.ui:id/home_header_long_qiandao_image");
    }

    /**
     * 点击自动签到
     *
     * @param nodeInfo
     * @param packageName
     * @param className
     */
    public static void policy(AccessibilityNodeInfo nodeInfo, String packageName, String className) {
        if (nodeInfo == null) {
            return;
        }

        if ("com.sinovatech.unicom.ui".equals(packageName)) {
            for (int i = 0; i < nodeInfo.getChildCount(); i++) {
                AccessibilityNodeInfo child = nodeInfo.getChild(i);
                if ("android.webkit.WebView".equals(child.getClassName())) {
                    Log.d(Config.TAG, "nodeInfo = " + nodeInfo.toString());
                    findEveryViewNode(child);
                    break;
                }
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

                String className = child.getViewIdResourceName();
                if ("qd_xq".equals(className)) {
                    Log.d(Config.TAG, "Button 的节点数据 text = " + child.getText() + ", descript = " + child.getContentDescription() + ", className = " + child.getClassName() + ", resId = " + child.getViewIdResourceName());

                    boolean isClickable = child.isClickable();

                    if ( isClickable) {
                        child.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        Log.d(Config.TAG, "联通签到 成功点击");
                    }
                }

                // 递归调用
                findEveryViewNode(child);
            }
        }
    }
}
