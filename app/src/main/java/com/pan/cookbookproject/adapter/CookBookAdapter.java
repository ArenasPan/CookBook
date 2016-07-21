package com.pan.cookbookproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pan.cookbookproject.R;
import com.pan.cookbookproject.bean.CookBookBean;

import java.util.List;

/**
 * Created by pan on 16/7/14.
 */
public class CookBookAdapter extends RecyclerView.Adapter<CookBookAdapter.ViewHolder> {

    private Context mContext;
    public List<CookBookBean> cookBookBeen;

    private OnItemClickListener mListener;

    public CookBookAdapter(Context mContext, List<CookBookBean> cookBookBeen) {
        this.mContext = mContext;
        this.cookBookBeen = cookBookBeen;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_cook_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tvName.setText(cookBookBeen.get(position).getName());
        holder.tvRemark.setText(cookBookBeen.get(position).getRemark());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClicked(view, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cookBookBeen.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.item_name);
            tvRemark = (TextView) itemView.findViewById(R.id.remark);
            view = (LinearLayout) itemView.findViewById(R.id.item_view);
        }

        private TextView tvName;
        private TextView tvRemark;
        private LinearLayout view;
    }

    public interface OnItemClickListener{
        public abstract void onItemClicked(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }
}
