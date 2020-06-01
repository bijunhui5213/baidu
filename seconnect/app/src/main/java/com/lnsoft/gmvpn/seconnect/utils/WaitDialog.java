package com.lnsoft.gmvpn.seconnect.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.lnsoft.gmvpn.seconnect.R;

public class WaitDialog extends Dialog {


    public WaitDialog(Context context) {
        super(context);//设置样式
        setCanceledOnTouchOutside(false);//按对话框以外的地方不起作用，按返回键可以取消对话框
        getWindow().setGravity(Gravity.CENTER);
        setContentView(R.layout.dialog_wait_layout);


    }




}

