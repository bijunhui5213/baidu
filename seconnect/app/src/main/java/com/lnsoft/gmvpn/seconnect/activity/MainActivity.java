package com.lnsoft.gmvpn.seconnect.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Base64DataException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.lnsoft.gmvpn.sdk.GMVpn;
import com.lnsoft.gmvpn.seconnect.R;
import com.lnsoft.gmvpn.seconnect.fragment.CertsFragment;
import com.lnsoft.gmvpn.seconnect.fragment.ImpowerFragment;
import com.lnsoft.gmvpn.seconnect.fragment.MainFragment;
import com.lnsoft.gmvpn.seconnect.fragment.NewSeconFragment;
import com.lnsoft.gmvpn.seconnect.fragment.VersionFragment;
import com.lnsoft.gmvpn.seconnect.item.CertItem;
import com.lnsoft.gmvpn.seconnect.item.SeconItem;
import com.lnsoft.gmvpn.seconnect.utils.FileUtil;
import com.lnsoft.gmvpn.seconnect.utils.WriteToFileUtils;

import org.bouncycastle.util.encoders.Base64Encoder;


import java.io.File;
import java.io.FileInputStream;
;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.security.Provider;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.com.sansec.key.SWXAPI;


public class MainActivity extends AppCompatActivity
        implements CertsFragment.OnFragmentInteractionListener, MainFragment.OnFragmentInteractionListener, NewSeconFragment.OnFragmentInteractionListener {


    /* 连接列表
     * */
    private List<SeconItem> mSeconItemList = new ArrayList<SeconItem>();


    private ImageView img;
    private TextView tv_title;
    private ConstraintLayout content_main;
    private NavigationView nv;
    private DrawerLayout drawer_layout;
    private TextView tv_new;
    private FrameLayout frameLayout;
    private RadioGroup rg;
    //记录用户首次点击返回键的时间
    private long firstTime = 0;
    private SeconItem si;
    private Certificate cert;
    private FileInputStream fileInputStream;
    private String sigAlgName;


    //底部 栏
    public RadioGroup getRg() {
        return rg;
    }

    //证书
    public List<CertItem> getmCertItemList() {
        return mCertItemList;
    }


    public Map<String, String> getmPfxPswMap() {
        return mPfxPswMap;
    }

    public void setmPfxPswMap(Map<String, String> mPfxPswMap) {
        this.mPfxPswMap = mPfxPswMap;
    }

    private Map<String, String> mPfxPswMap = new HashMap<String, String>();

    /*证书列表
     * */
    private List<CertItem> mCertItemList = new ArrayList<CertItem>();

    //编辑连接集合
    public List<SeconItem> getmSeconItemList() {
        return mSeconItemList;
    }

    //SDK
    public GMVpn getmGMVpn() {
        return mGMVpn;
    }

    private GMVpn mGMVpn;

    public int getmEdtingSeconIndex() {
        return mEdtingSeconIndex;
    }

    public void setmEdtingSeconIndex(int mEdtingSeconIndex) {
        this.mEdtingSeconIndex = mEdtingSeconIndex;
    }

    private int mEdtingSeconIndex = -1;


    public int getmActiveSeconIndex() {
        return mActiveSeconIndex;
    }

    public void setmActiveSeconIndex(int mActiveSeconIndex) {
        this.mActiveSeconIndex = mActiveSeconIndex;
    }

    /* 已连接的index，-1：表示都没有连接，else：为连接的index
     * */
    private int mActiveSeconIndex = -1;

    public String getmActiveSeconStatus() {
        return mActiveSeconStatus;
    }

    public void setmActiveSeconStatus(String mActiveSeconStatus) {
        this.mActiveSeconStatus = mActiveSeconStatus;

    }


    public void setSeconStatus(String status) {
        if (mActiveSeconIndex >= 0) {
            mSeconItemList.get(mActiveSeconIndex).setSeconStatus(status);
        }

    }

    public void setSeconIcon(int icon) {
        if (mActiveSeconIndex >= 0) {
            mSeconItemList.get(mActiveSeconIndex).setImageID(icon);
        }
    }


    private String mActiveSeconStatus = "未连接";

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFile();

        initView();

        //sdk库
        Version();
        // 动态申请外部存储目录的读、写权限，用户不需要再去“设置”里查找相应的设置项目。

        Jurisdiction();
        //获取系统文件


        initFragment();

    }

    private void initFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new MainFragment()).commit();
        readSeconsFromFile();

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new MainFragment()).commit();
                        readSeconsFromFile();
                        break;
                    case R.id.rb_2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new CertsFragment()).commit();
                        break;
                    case R.id.rb_3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new VersionFragment()).commit();
                        break;
                }
            }
        });

    }


    private void initFile() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                //初始化文件路径
                //File filePath = getFilesDir();
                //FileUtil.GMVPN_ROOTPATH = filePath.toString();
                FileUtil.GMVPN_ROOTPATH = "/sdcard/gmvpn";
                //设置&创建证书存放路径
                FileUtil.GMVPN_CERTPATH = FileUtil.GMVPN_ROOTPATH + "/certs/";
                File file = new File(FileUtil.GMVPN_CERTPATH);
                if (!file.exists()) {
                    Log.e("创建了路径：", file.toString());
                    file.mkdirs();
                }
                //显示文件
                File[] items = file.listFiles();
                if (null != items) {
                    for (File item : items) {
                        Log.e("发现文件：", item.getName());
                    }
                }

                //设置&创建连接存放路径
                FileUtil.GMVPN_CONPATH = FileUtil.GMVPN_ROOTPATH + "/cons/";
                file = new File(FileUtil.GMVPN_CONPATH);
                if (!file.exists()) {
                    file.mkdirs();
                }
                //创建data路径
                FileUtil.GMVPN_DATAPATH = FileUtil.GMVPN_ROOTPATH + "/data/";
                file = new File(FileUtil.GMVPN_DATAPATH);
                if (!file.exists()) {
                    file.mkdirs();
                }
                FileUtil.GMVPN_PFXPSWFILE = FileUtil.GMVPN_DATAPATH + "data.file";

                file = new File(FileUtil.GMVPN_CONPATH);
                Log.e("遍历的目录：", file.toString());
                if (!file.exists()) {
                    Log.e("创建了路径：", file.toString());
                    file.mkdirs();
                }
                //显示filePath下的目录
                items = file.listFiles();
                if (null != items) {
                    for (File item : items) {
                        Log.e("发现文件：", item.getName());
                    }
                }
                //初始化GMVPN实例
                mGMVpn = new GMVpn(getApplicationContext());


            }

        }.start();

    }

    private void Jurisdiction() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }


    }

    private void Version() {
        //引入库
        int version = Build.VERSION.SDK_INT;
        //19
        if (version >= 19) {
            String strAPPFileDirs = null;
            this.getExternalFilesDir(strAPPFileDirs);
            //set sd path and package name
            SWXAPI.setPackageName(getPackageName());
        }
    }

    private void initView() {


        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);

        rg = (RadioGroup) findViewById(R.id.rg);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onFragmentInteraction(Uri uri) {


    }

    public void readSeconsFromFile() {
        mSeconItemList.clear();
        if (FileUtil.GMVPN_CONPATH != null) {
            File file = new File(FileUtil.GMVPN_CONPATH);
            if (file != null) {
                String fileFullPath;
                Log.e("遍历的目录：", file.toString());
                if (!file.exists()) {
                    file.mkdirs();
                }
                //显示filePath下的目录
                File[] items = file.listFiles();
                if (null != items) {
                    for (File item : items) {
                        fileFullPath = FileUtil.GMVPN_CONPATH + item.getName();
                        Log.e("发现文件：", fileFullPath);
                        try {
                            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileFullPath));

                            si = (SeconItem) ois.readObject();

                            //初始化列表状态与图标
                            si.setImageID(R.drawable.secon_iconun);
                            si.setSeconStatus("未连接");
                            mSeconItemList.add(si);
                            ois.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (mActiveSeconIndex >= 0) {
                        //设置连接成功后的状态
                        mSeconItemList.get(mActiveSeconIndex).setSeconStatus(mActiveSeconStatus);
                        //设置连接成功后的图标
                        mSeconItemList.get(mActiveSeconIndex).setImageID(R.drawable.secon_icon);
                    }
                }
            }
        }
    }

    //初始化数据
    public void handleData() {

        //先清空一下
        mPfxPswMap.clear();
        //读取密码文件
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FileUtil.GMVPN_PFXPSWFILE));
            mPfxPswMap = (HashMap) ois.readObject();
            for (String key : mPfxPswMap.keySet()) {
                Log.e("PFX文件：", key + ": " + mPfxPswMap.get(key));
            }
            ois.close();
        } catch (Exception e) {
        }

        //清空已有数据
        mCertItemList.clear();
        //遍历查看文件
        //文件路径
        File file = new File(FileUtil.GMVPN_CERTPATH);
        Log.e("遍历的目录：", file.getPath());
        File[] items = file.listFiles();
        String certName;


        if (null != items) {

            for (int i = 0; i < items.length; i++) {
                Log.e("发现文件：", items[i].getName());
                //文件名字
                certName = items[i].getName();
                CertItem it = new CertItem(items[i].getName(), "", R.mipmap.certs, items[i].getPath());
                it.setmCertPSW(mPfxPswMap.get(certName));
                Log.e("文件名--密码： ", certName + "--" + mPfxPswMap.get(certName));
                mCertItemList.add(it);

            }
            for (int i = 0; i < mCertItemList.size(); i++) {
                //证书路径
                List<String> str = WriteToFileUtils.getStr(mCertItemList.get(i).getCertPath());
                StringBuilder aa= new StringBuilder();

                for (int j = 0; j < str.size(); j++) {
                    aa.append(str.get(j));
                }

                try {
                    fileInputStream = new FileInputStream(mCertItemList.get(i).getCertPath());
                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
                    Collection c = (Collection) cf.generateCertificates(fileInputStream);
                    Iterator iterator = c.iterator();
                    while (iterator.hasNext()) {
                        //证书信息
                        cert = (Certificate)iterator.next();
                    }
                    System.out.println(cert);



                    fileInputStream.close();
                } catch (CertificateException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e("bjh证书信息", "handleData: "+cert);
            }

        }

    }


    public void savePfxPsw() {
        String path = FileUtil.GMVPN_PFXPSWFILE;
        try {
            ObjectOutputStream oos1 = new ObjectOutputStream(new FileOutputStream(path));
            oos1.writeObject(mPfxPswMap);
            oos1.close();
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }

    //提示退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mGMVpn.release();

    }


}
