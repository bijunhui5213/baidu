package com.lnsoft.gmvpn.seconnect.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lnsoft.gmvpn.seconnect.R;
import com.lnsoft.gmvpn.seconnect.item.CertItem;

import java.util.ArrayList;
import java.util.List;

public class SpinCertItemAdapter extends BaseAdapter {
    private int resourceId;
    private Context mContext;
    private List<CertItem> mList = new ArrayList<>();

    public void setmList(List<CertItem> mList) {
        this.mList = mList;
    }

    // 适配器的构造函数，把要适配的数据传入这里
    public SpinCertItemAdapter(Context context, int textViewResourceId, List<CertItem> list){
        resourceId=textViewResourceId;
        mContext = context;
        mList = list;
    }
    @Override
    public int getCount(){
        return  mList.size();
    }
    @Override
    public long getItemId(int i){
        return i;
    }
    @Override
    public CertItem getItem(int i){
        return mList.get(i);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LinearLayout ll = new LinearLayout( mContext );
        ll.setOrientation(LinearLayout. HORIZONTAL );
        ll.setGravity(Gravity. CENTER_VERTICAL );
        ImageView iv = new ImageView( mContext );
        iv.setImageResource( R.drawable.ic_cert);
        iv.setLayoutParams( new ViewGroup.LayoutParams(100, 40));
        ll.addView(iv);
        TextView tv = new TextView( mContext );
        tv.setText( mList.get(position).getCertName());
        tv.setTextSize(18);
        //设置最大显示
        tv.setMaxEms(12);
        tv.setMaxLines(1);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setTextColor(Color.parseColor("#006569"));
        ll.addView(tv);
        return ll;
    }

}
