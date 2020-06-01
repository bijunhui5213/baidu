package com.lnsoft.gmvpn.seconnect.activity;

import android.annotation.SuppressLint;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lnsoft.gmvpn.seconnect.R;
import com.lnsoft.gmvpn.seconnect.adapter.RvAdapter;
import com.lnsoft.gmvpn.seconnect.bean.AppInfo;
import com.lnsoft.gmvpn.seconnect.bean.DbBean;
import com.lnsoft.gmvpn.seconnect.utils.DbUtils;

import java.util.ArrayList;

import java.util.List;


public class AuthorizationActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_title;
    private Toolbar toolbar;
    private ArrayList<AppInfo> list;
    private RecyclerView rv;
    private RvAdapter adapter;
    private ImageView img_back;
    private Button bt;
    private AppInfo appInfo;
    private List<DbBean> dbBeans;
    private SwitchCompat checkbox_all;
    private DbBean dbBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setEnterTransition(new Explode());
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_authorization);
        dbBeans = DbUtils.getUtils().queryAll();
        initView();
        getPackages();
        if (dbBeans != null) {
            adapter.setDbBeans(dbBeans);
        } else {
            adapter.setList(list);
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dbBeans != null) {
            adapter.setDbBeans(dbBeans);
        } else {
            adapter.setList(list);
        }
        adapter.notifyDataSetChanged();

    }

    private void getPackages() {
        // 获取已经安装的所有应用, PackageInfo　系统类，包含应用信息
        List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) { //非系统应用
                // AppInfo 自定义类，包含应用信息
                appInfo = new AppInfo();
                appInfo.setAppName(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString());//获取应用名称
                appInfo.setPackageName(packageInfo.packageName); //获取应用包名，可用于卸载和启动应用
                appInfo.setVersionName(packageInfo.versionName);//获取应用版本名
                appInfo.setVersionCode(packageInfo.versionCode);//获取应用版本号
                appInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(getPackageManager()));//获取应用图标
                list.add(appInfo);
                dbBean = new DbBean();
                dbBean.setAppName(appInfo.getAppName());
                dbBean.setPackageName(appInfo.getPackageName());
                dbBean.setIsCheck(appInfo.isCheck());
                DbUtils.getUtils().insert(dbBean);


            }  // 系统应用

        }
    }


    @SuppressLint("NewApi")
    private void initView() {

        tv_title = (TextView) findViewById(R.id.tv_title);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        rv = (RecyclerView) findViewById(R.id.rv);
        list = new ArrayList<>();
        adapter = new RvAdapter(this, list);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rv.setAdapter(adapter);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        bt = (Button) findViewById(R.id.bt);
        bt.setOnClickListener(this);

        //全选按钮
        checkbox_all = (SwitchCompat) findViewById(R.id.checkbox_all);
        //保存全选按钮的状态  如果列表是全选则全选按钮为true
        for (int i = 0; i < dbBeans.size(); i++) {
            if (dbBeans.get(i).getIsCheck()) {
                checkbox_all.setChecked(true);
            } else {
                checkbox_all.setChecked(false);
            }
        }

        checkbox_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //全选
                if (checkbox_all.isChecked()) {
                    for (int i = 0; i < dbBeans.size(); i++) {
                        dbBeans.get(i).setIsCheck(true);
                        dbBean.setIsCheck(true);
                    }

                } else {
                    for (int i = 0; i < dbBeans.size(); i++) {
                        dbBeans.get(i).setIsCheck(false);
                        dbBean.setIsCheck(false);
                    }
                }
                adapter.notifyDataSetChanged();
            }

        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt:
                finish();
                break;
            case R.id.img_back:
                finish();
                break;

        }
    }

}
