package com.lnsoft.gmvpn.seconnect.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lnsoft.gmvpn.seconnect.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VersionFragment extends Fragment {


    private View inflate;

    public VersionFragment() {
        // Required empty public constructor
    }
    private ImageView img;
    private TextView tv_title;
    private TextView tv_version;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_version, container, false);
        initView();
        return inflate;
    }
    @SuppressLint("SetTextI18n")
    private void initView() {
        img = (ImageView) inflate.findViewById(R.id.img);
        tv_title = (TextView)inflate. findViewById(R.id.tv_title);
        tv_version = (TextView) inflate.findViewById(R.id.tv_version);
        String verName = getVerName(getActivity());
        tv_version.setText("Version  " + verName);





    }
    private static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

}
