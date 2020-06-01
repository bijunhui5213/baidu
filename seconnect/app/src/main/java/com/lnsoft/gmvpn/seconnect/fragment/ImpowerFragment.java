package com.lnsoft.gmvpn.seconnect.fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lnsoft.gmvpn.seconnect.R;
import com.lnsoft.gmvpn.seconnect.activity.MainActivity;
import com.lnsoft.gmvpn.seconnect.adapter.RvAdapter;
import com.lnsoft.gmvpn.seconnect.bean.AppInfo;
import com.lnsoft.gmvpn.seconnect.bean.DbBean;
import com.lnsoft.gmvpn.seconnect.utils.DbUtils;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImpowerFragment extends Fragment {


    private RecyclerView rv;
    private AppInfo appInfo;
    private List<DbBean> dbBeans;
    private ArrayList<AppInfo> list;
    private RvAdapter adapter;
    private View inflate;
    private MainActivity mainActivity;

    public ImpowerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_impower, container, false);
        initView();
        getPackages();
        dbBeans = DbUtils.getUtils().queryAll();
        if (dbBeans != null) {
            adapter.setDbBeans(dbBeans);
        } else {
            adapter.setList(list);
        }
        adapter.notifyDataSetChanged();
        //必须断开连接才能访问
        mainActivity = (MainActivity) getActivity();
        if (inflate != null) {
            if (mainActivity.getmActiveSeconIndex() != -1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("连接必须是断开状态才可操作，要断开连接吗？");

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //什么都不做
                        mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new MainFragment()).commit();

                    }
                });
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //断开连接
                        if (mainActivity != null) {
                            mainActivity.getmGMVpn().disconnect();
                        }
                    }
                });
                builder.show();
            }
        }

        return inflate;

    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        dbBeans = DbUtils.getUtils().queryAll();
        outState.putBoolean("iscd", dbBeans.get(0).getIsCheck());
    }

    private void getPackages() {
        // 获取已经安装的所有应用, PackageInfo　系统类，包含应用信息
        List<PackageInfo> packages = Objects.requireNonNull(getActivity()).getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) { //非系统应用
                // AppInfo 自定义类，包含应用信息
                appInfo = new AppInfo();
                appInfo.setAppName(packageInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString());//获取应用名称
                appInfo.setPackageName(packageInfo.packageName); //获取应用包名，可用于卸载和启动应用
                appInfo.setVersionName(packageInfo.versionName);//获取应用版本名
                appInfo.setVersionCode(packageInfo.versionCode);//获取应用版本号
                appInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(getActivity().getPackageManager()));//获取应用图标
                list.add(appInfo);
                DbBean dbBean = new DbBean();
                dbBean.setAppName(appInfo.getAppName());
                dbBean.setPackageName(appInfo.getPackageName());
                dbBean.setIsCheck(appInfo.isCheck());
                DbUtils.getUtils().insert(dbBean);

            } else { // 系统应用


            }
        }
    }

    private void initView() {
        rv = (RecyclerView) inflate.findViewById(R.id.rv);
        list = new ArrayList<>();
        adapter = new RvAdapter(getActivity(),list);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        //设置分割线
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rv.setAdapter(adapter);


    }


}
