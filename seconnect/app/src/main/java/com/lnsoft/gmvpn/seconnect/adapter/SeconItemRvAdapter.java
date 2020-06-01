package com.lnsoft.gmvpn.seconnect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lnsoft.gmvpn.seconnect.R;
import com.lnsoft.gmvpn.seconnect.item.SeconItem;

import java.util.ArrayList;
import java.util.List;

public class SeconItemRvAdapter extends RecyclerView.Adapter<SeconItemRvAdapter.ViewHolder> {
    private Context context;
    private List<SeconItem> list;

    public SeconItemRvAdapter(Context context, List<SeconItem> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(List<SeconItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.secon_list, null);
        ViewHolder viewHolder = new ViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.seconItemImg.setImageResource(list.get(position).getImageID());
        holder.seconItemName.setText(list.get(position).getmSeconName());
        holder.seconItemStatus.setText(list.get(position).getSeconStatus());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view != null) {
                    onClickListener.onClick(position);


                }
            }
        });
        holder.img_xiugai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view != null){
                    onClickImg.onClick(position);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView seconItemImg;
        private TextView seconItemName;
        private TextView seconItemStatus;
        private ImageView img_xiugai;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            seconItemImg = itemView.findViewById(R.id.secon_image);
            seconItemName = itemView.findViewById(R.id.secon_name);
            seconItemStatus = itemView.findViewById(R.id.secon_status);
            img_xiugai = itemView.findViewById(R.id.img_xiugai);
        }
    }

    public onClickListener onClickListener;

    public void setOnClickListener(SeconItemRvAdapter.onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    public interface onClickListener {
        void onClick(int position);
    }


    public onClickImg onClickImg;

    public void setOnClickImg(SeconItemRvAdapter.onClickImg onClickImg) {
        this.onClickImg = onClickImg;
    }

    public interface onClickImg{
        void onClick(int position);
    }
}
