package com.flyworkspace.apkinfo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by jinpengfei on 16-1-20.
 */
public class PublicFunction {
    public static void copy(String content, Context context) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setPrimaryClip(ClipData.newPlainText(null, content.trim()));
    }

    public static String getPackageName(Context context) {
        String AppVersion = null;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            AppVersion = String.valueOf(info.packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return AppVersion;
    }

    private static String getAppVersion(Context context) {
        String AppVersion = "1";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            AppVersion = String.valueOf(info.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            AppVersion = "1";
        }
        return AppVersion;
    }
}
