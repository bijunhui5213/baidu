package com.lnsoft.gmvpn.seconnect.item;

import java.io.Serializable;

public class CertItem implements Serializable {
    private String mCertName;
    private String mCertCN;
    private int mImageID;
    private String mCertPath;

    public String getmCertPSW() {
        return mCertPSW;
    }

    public void setmCertPSW(String mCertPSW) {
        this.mCertPSW = mCertPSW;
    }

    private String mCertPSW;

    public CertItem(String name, String cn, int imgid, String path) {
        this.mCertName = name;
        this.mCertCN = cn;
        this.mImageID = imgid;
        this.mCertPath = path;
    }

    public String getCertName() {
        return mCertName;
    }

    public String getrCertCN() {
        return mCertCN;
    }

    public int getImageID() {
        return mImageID;
    }

    public String getCertPath() {
        return mCertPath;
    }


    /*证书比较，若名称一致，则认为是同一证书*/
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CertItem) {
            if (this.getCertName().equals(((CertItem) obj).getCertName())) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }


}
