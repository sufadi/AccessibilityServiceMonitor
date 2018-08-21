package com.fadi.forestautoget.service;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.fadi.forestautoget.util.Config;

import java.util.List;

public class KeepAppMonitor {

    public static void policy(AccessibilityNodeInfo nodeInfo, String packageName, String className) {
        if (!("com.gotokeep.keep".equals(packageName) &&
                "android.support.v7.widget.RecyclerView".equals(className))) {
            return;
        }

        // 关注界面的点赞
        keepAppPraise(nodeInfo,"com.gotokeep.keep:id/item_cell_praise_container");

        // 好友界面的点赞
        keepAppPraise(nodeInfo,"com.gotokeep.keep:id/stroke_view");

        // 热点界面的点赞
        keepAppPraise(nodeInfo,"com.gotokeep.keep:id/layout_like");
    }

    public static void keepAppPraise(AccessibilityNodeInfo nodeInfo, String id) {
        if (nodeInfo != null) {
            // 该界面下所有 ViewId 节点
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(id);
            for (AccessibilityNodeInfo item : list) {
                if (item.isClickable()) {
                    Log.d(Config.TAG, "keepAppPraise = " + item.getClassName());
                    item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }
    }
}
