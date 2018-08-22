package com.fadi.forestautoget.util;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class CommonUtil {

    public static void clickBtnByResId(AccessibilityNodeInfo nodeInfo, String id) {
        if (nodeInfo != null) {
            // 该界面下所有 ViewId 节点
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(id);
            for (AccessibilityNodeInfo item : list) {
                if (item.isClickable()) {
                    item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    Log.d(Config.TAG, "clickBtnByResId = " + item.toString());
                    break;
                }
            }
        }
    }
}
