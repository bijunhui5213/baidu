package com.lnsoft.gmvpn.seconnect.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.lnsoft.gmvpn.seconnect.R;

public class WelcomeActivity extends AppCompatActivity {

    private ImageView welcome_img;
    private  int i = 1;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x001) {
                if (i > 0) {
                    i--;
                    handler.sendEmptyMessageDelayed(0x001, 1000);
                }else{
                    startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                    finish();
                }
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
    }

    private void initView() {
        welcome_img = (ImageView) findViewById(R.id.welcome_img);
        handler.sendEmptyMessageDelayed(0x001, 1000);


    }
}
