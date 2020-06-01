package com.lnsoft.gmvpn.seconnect.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.lnsoft.gmvpn.seconnect.R;

public class VersionActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView img;
    private TextView tv_title;
    private TextView tv_version;
    private TextView tv_toolbar;
    private Toolbar toolbar;
    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);
        initView();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        img = (ImageView) findViewById(R.id.img);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_version = (TextView) findViewById(R.id.tv_version);
        String verName = getVerName(this);
        tv_version.setText("Version  " + verName);
        tv_toolbar = (TextView) findViewById(R.id.tv_toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        img.setOnClickListener(this);

        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
    }


    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.img_back) {
            finish();
        }
    }
}
