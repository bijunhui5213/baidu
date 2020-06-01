package com.lnsoft.gmvpn.seconnect.base;

import android.app.Application;

import com.lnsoft.gmvpn.sdk.core.GMVpnApplication;

public class SeconApplicaton  extends Application {
    private static SeconApplicaton applicaton ;
    @Override
    public void onCreate() {
        super.onCreate();
        GMVpnApplication.init(this);
        applicaton = this;
        this.getClass().getPackage().getName();
    }

    public static SeconApplicaton getApplicaton() {
        return applicaton;
    }


}
