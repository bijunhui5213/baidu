package com.lnsoft.gmvpn.seconnect.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lnsoft.gmvpn.seconnect.R;
import com.lnsoft.gmvpn.seconnect.activity.AuthorizationActivity;
import com.lnsoft.gmvpn.seconnect.activity.MainActivity;
import com.lnsoft.gmvpn.seconnect.bean.AppInfo;
import com.lnsoft.gmvpn.seconnect.bean.DbBean;
import com.lnsoft.gmvpn.seconnect.fragment.MainFragment;
import com.lnsoft.gmvpn.seconnect.utils.DbUtils;
import com.lnsoft.gmvpn.seconnect.utils.SpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.ViewHolder> {
    private Context context;
    private List<AppInfo> list;
    private List<String> strList = new ArrayList<>();
    private List<DbBean> dbBeans;
    private DbBean dbBean;

    public void setDbBeans(List<DbBean> dbBeans) {
        this.dbBeans = dbBeans;

        strList.clear();
        for (int i = 0; i < dbBeans.size(); i++) {
            if (dbBeans.get(i).getIsCheck()) {
                strList.add(dbBeans.get(i).getPackageName());
            }
        }
    }

    public RvAdapter(Context context, ArrayList<AppInfo> list) {
        this.context = context;
        this.list = list;
        strList.clear();


    }

    public void setList(List<AppInfo> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.infoapp_item, null);
        ViewHolder viewHolder = new ViewHolder(inflate);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        // holder.setIsRecyclable(false);
        holder.checkBox.setOnCheckedChangeListener(null);
        Glide.with(context).load(list.get(position).getAppIcon()).into(holder.img);
        holder.tv.setText(list.get(position).getAppName());


        //设置状态

        holder.checkBox.setChecked(dbBeans.get(position).getIsCheck());

        //checkbox点击事件
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            public void onCheckedChanged(CompoundButton arg0, final boolean isChecked) {

                String strApps = "";

                dbBean = dbBeans.get(position);
                //选中获取包名
                if (isChecked) {
                    //数据库保存状态
                    dbBean.setIsCheck(true);
                    //选中的包名
                    strApps = list.get(position).getPackageName();
                    //添加到新的集合中
                    strList.add(strApps);
                } else {//移除
                    dbBean.setIsCheck(false);
                    strApps = list.get(position).getPackageName();
                    strList.remove(strApps);

                }

                //数据库保存状态
                List<DbBean> dbBeans = DbUtils.getUtils().queryAll();
                if (dbBeans != null) {
                    //设置状态
                    dbBeans.get(position).setIsCheck(isChecked);
                }


                //将集合传到字符串
                strApps = "";
                if (!strList.isEmpty()) {
                    for (int i = 0; i < strList.size(); i++) {
                        //拼接字符串
                        strApps += strList.get(i);
                        strApps += ":";
                    }
                    strApps = strApps.substring(0, strApps.lastIndexOf(":"));
                    Log.e("bjh: ", strApps);
                }

                SpUtil.setParam("listCheck", strApps);

            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView tv;
        private CheckBox checkBox;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            tv = itemView.findViewById(R.id.tv);
            checkBox = itemView.findViewById(R.id.checkbox);

        }
    }
}
