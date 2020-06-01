package com.lnsoft.gmvpn.seconnect.item;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.lnsoft.gmvpn.sdk.GMVpnConfig;
import com.lnsoft.gmvpn.seconnect.R;
import com.lnsoft.gmvpn.seconnect.base.SeconApplicaton;
import com.lnsoft.gmvpn.seconnect.bean.AppInfo;
import com.lnsoft.gmvpn.seconnect.utils.SpUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.lnsoft.gmvpn.sdk.VpnStatusChangedListener.VPN_STATUS_CONNECTED;

public class SeconItem implements Serializable {
    private String mSeconName;
    private String mSeconStatus;    //连接状态描述
    private int mImageID;

    private String mSeconAddr;
    private String mSeconPort;
    private String mSeconProto;
    private String mAuthType;
    private CertItem mCertChain;
    private CertItem mSignCert;
    private CertItem mEncCert;


    public String getmHSMPin() {
        return mHSMPin;
    }

    public void setmHSMPin(String mHSMPin) {
        this.mHSMPin = mHSMPin;
    }


    private String mHSMPin;

    public String getmHSMSignContainer() {
        return mHSMSignContainer;
    }

    public void setmHSMSignContainer(String mHSMSignContainer) {
        this.mHSMSignContainer = mHSMSignContainer;
    }

    private String mHSMSignContainer;

    public String getmHSMEncContainer() {
        return mHSMEncContainer;
    }

    public void setmHSMEncContainer(String mHSMEncContainer) {
        this.mHSMEncContainer = mHSMEncContainer;
    }

    private String mHSMEncContainer;


    private String mSeconPath;
    private int mStatus;            //连接状态

    public int getmStatus() {
        return mStatus;
    }

    public void setmStatus(int mStatus) {
        this.mStatus = mStatus;
    }


    public String getmSeconPath() {
        return mSeconPath;
    }

    public void setmSeconPath(String mSeconPath) {
        this.mSeconPath = mSeconPath;
    }


    public SeconItem() {

    }

    public SeconItem(String name, String status, int imgid) {
        this.mSeconName = name;
        this.mSeconStatus = status;
        this.mImageID = imgid;
    }

    public String getSeconStatus() {
        return mSeconStatus;
    }

    public void setSeconStatus(String status) {
        mSeconStatus = status;
    }

    public int getImageID() {
        return mImageID;
    }

    public void setImageID(int id) {
        mImageID = id;
    }


    public String getmSeconName() {
        return mSeconName;
    }

    public void setmSeconName(String mSeconName) {
        this.mSeconName = mSeconName;
    }

    public String getmSeconAddr() {
        return mSeconAddr;
    }

    public void setmSeconAddr(String mSeconAddr) {
        this.mSeconAddr = mSeconAddr;
    }

    public String getmSeconPort() {
        return mSeconPort;
    }

    public void setmSeconPort(String mSeconPort) {
        this.mSeconPort = mSeconPort;
    }

    public String getmSeconProto() {
        return mSeconProto;
    }

    public void setmSeconProto(String mSeconProto) {
        this.mSeconProto = mSeconProto;
    }

    public String getmAuthType() {
        return mAuthType;
    }

    public void setmAuthType(String mAuthType) {
        this.mAuthType = mAuthType;
    }

    public CertItem getmCertChain() {
        return mCertChain;
    }

    public void setmCertChain(CertItem mCertChain) {
        this.mCertChain = mCertChain;
    }

    public CertItem getmSignCert() {
        return mSignCert;
    }

    public void setmSignCert(CertItem mSignCert) {
        this.mSignCert = mSignCert;
    }

    public CertItem getmEncCert() {
        return mEncCert;
    }

    public void setmEncCert(CertItem mEncCert) {
        this.mEncCert = mEncCert;
    }

    private List<String> apps = new ArrayList<>();

    public GMVpnConfig.Builder buildConfig() {
        final GMVpnConfig.Builder builder = new GMVpnConfig.Builder();
        builder.setProtocol(mSeconProto);
        builder.setAddress(mSeconAddr, mSeconPort);
        // 证书密码
        //builder.setKeyPass("123456", "123456");
        // 填写完整的文件路径
//         Log.e("根证书：--",mCertChain.getCertPath()+"。");
        //builder.setFilePath("/sdcard/gmvpn/certs/root.crt", "/sdcard/gmvpn/certs/test_sign.pfx", "/sdcard/gmvpn/certs/test_enc.pfx");
        final String packageName = SeconApplicaton.getApplicaton().getPackageName();

        String listCheck = (String) SpUtil.getParam("listCheck", "");
        //获取字符串
        if(listCheck != null && !listCheck.equals("")){
            String[] split = listCheck.split(":");

            //以下这俩方式相同
            Collections.addAll(apps, split);
           /* for (int i = 0; i <split.length ; i++) {
                apps.add(split[i]);
            }*/
        }
        apps.add(listCheck);


        Log.e("bjh", "onClick: " + apps);

        if (null != mAuthType) {
            if (mAuthType.equals("硬件证书")) {
                if (packageName != null || mHSMSignContainer != null || mHSMPin != null || mCertChain.getCertPath() != null) {

                    Log.e("TFKEY认证码：--", mHSMPin + "");
                    Log.e("签名容器：--", mHSMSignContainer+"");
                    Log.e("加密容器：--", mHSMEncContainer+"");

                    //模式认证方式
                    builder.setAuthenType(GMVpnConfig.AUTHEN_TYPE_TFKEY);
                    builder.setKeyPass(mHSMPin, mHSMPin);
                    builder.setSDKey(packageName + "", mHSMSignContainer + "", mHSMPin + "", mCertChain.getCertPath() + "");
                    builder.setAllowedApps(apps);


                }
            } else {

                if (packageName != null || mHSMSignContainer != null || mHSMPin != null || mCertChain.getCertPath() != null) {
                    Log.e("签名证书：--", mSignCert.getCertPath() + ": " + mSignCert.getmCertPSW());
                    Log.e("加密证书：--", mEncCert.getCertPath() + ": " + mSignCert.getmCertPSW());

                    builder.setAuthenType(GMVpnConfig.AUTHEN_TYPE_FILE_PKCS12);
                    builder.setKeyPass(mSignCert.getmCertPSW(), mEncCert.getmCertPSW());
                    builder.setFilePath(mCertChain.getCertPath(), mSignCert.getCertPath(), mEncCert.getCertPath());
                    builder.setAllowedApps(apps);


                }
            }
        }
        return builder;
    }


}



