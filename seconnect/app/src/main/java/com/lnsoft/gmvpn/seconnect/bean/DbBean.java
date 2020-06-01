package com.lnsoft.gmvpn.seconnect.bean;

import android.graphics.drawable.Drawable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Keep;

@Entity
public class DbBean {
    @Id(autoincrement = true)
    private Long id;
    private  String AppName;
    private String PackageName;
    private String VersionName;
    private int VersionCode;
    private Integer AppIcon;
    @Keep
    private boolean isCheck;
    @Generated(hash = 80605576)
    public DbBean(Long id, String AppName, String PackageName, String VersionName,
            int VersionCode, Integer AppIcon, boolean isCheck) {
        this.id = id;
        this.AppName = AppName;
        this.PackageName = PackageName;
        this.VersionName = VersionName;
        this.VersionCode = VersionCode;
        this.AppIcon = AppIcon;
        this.isCheck = isCheck;
    }
    @Generated(hash = 1953169116)
    public DbBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getAppName() {
        return this.AppName;
    }
    public void setAppName(String AppName) {
        this.AppName = AppName;
    }
    public String getPackageName() {
        return this.PackageName;
    }
    public void setPackageName(String PackageName) {
        this.PackageName = PackageName;
    }
    public String getVersionName() {
        return this.VersionName;
    }
    public void setVersionName(String VersionName) {
        this.VersionName = VersionName;
    }
    public int getVersionCode() {
        return this.VersionCode;
    }
    public void setVersionCode(int VersionCode) {
        this.VersionCode = VersionCode;
    }
    public Integer getAppIcon() {
        return this.AppIcon;
    }
    public void setAppIcon(Integer AppIcon) {
        this.AppIcon = AppIcon;
    }
    public boolean getIsCheck() {
        return this.isCheck;
    }
    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    @Override
    public String toString() {
        return "DbBean{" +
                "id=" + id +
                ", AppName='" + AppName + '\'' +
                ", PackageName='" + PackageName + '\'' +
                ", VersionName='" + VersionName + '\'' +
                ", VersionCode=" + VersionCode +
                ", AppIcon=" + AppIcon +
                ", isCheck=" + isCheck +
                '}';
    }
}
