package com.lnsoft.gmvpn.seconnect.bean;

import android.graphics.drawable.Drawable;

import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.lang.annotation.Target;

public  class   AppInfo  implements Serializable  {
    private static final long serialVersionUID = 3694902274397865665L;
    private  String AppName;
    private String PackageName;
    private String VersionName;
    private int VersionCode;
    private Drawable AppIcon;
    private boolean isCheck;


    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }

    public String getPackageName() {
        return PackageName;
    }

    public void setPackageName(String packageName) {
        PackageName = packageName;
    }

    public String getVersionName() {
        return VersionName;
    }

    public void setVersionName(String versionName) {
        VersionName = versionName;
    }

    public int getVersionCode() {
        return VersionCode;
    }

    public void setVersionCode(int versionCode) {
        VersionCode = versionCode;
    }

    public Drawable getAppIcon() {
        return AppIcon;
    }


    public void setAppIcon(Drawable appIcon) {
        AppIcon = appIcon;
    }

}
