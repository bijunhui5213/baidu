package com.lnsoft.gmvpn.seconnect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lnsoft.gmvpn.seconnect.R;
import com.lnsoft.gmvpn.seconnect.item.CertItem;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CertItemRvAdapter extends RecyclerView.Adapter<CertItemRvAdapter.ViewHolder> {
    private Context context;
    private int resourceId;
    private List<CertItem> list = new ArrayList<>();


    public CertItemRvAdapter(Context context, int textViewResourceId, List<CertItem> list) {
        this.context = context;
        this.resourceId=textViewResourceId;
        this.list = list;
        notifyDataSetChanged();

    }

    public void setList(List<CertItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.cert_list, null);
        ViewHolder viewHolder = new ViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.cert_image.setImageResource(list.get(position).getImageID());
        holder.cert_name.setText(list.get(position).getCertName());
        holder.cert_cn.setText(list.get(position).getrCertCN());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView cert_image;
        private TextView cert_name;
        private TextView cert_cn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cert_image = itemView.findViewById(R.id.cert_image);
            cert_name = itemView.findViewById(R.id.cert_name);
            cert_cn = itemView.findViewById(R.id.cert_cn);

        }
    }

}
