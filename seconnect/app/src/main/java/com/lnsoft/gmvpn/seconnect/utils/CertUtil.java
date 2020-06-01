package com.lnsoft.gmvpn.seconnect.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;


import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.Security;

public class CertUtil {
    public static boolean checkPfxPsw(String pfxFile,String pfxPSW)
    {
        /*
        BouncyCastleProvider provider = new BouncyCastleProvider();
        Security.addProvider(provider);
        try{
            KeyStore store = KeyStore.getInstance("PKCS12", "BC");
            store.load(new FileInputStream(pfxFile),pfxPSW.toCharArray());
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }*/
        return true;
    }

    public static void main(String[] args) {

    }


}
