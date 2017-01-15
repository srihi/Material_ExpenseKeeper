package com.library.ironwill.expensekeeper.adapter;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.library.ironwill.expensekeeper.R;
import com.library.ironwill.expensekeeper.model.ItemCategory;
import com.library.ironwill.expensekeeper.view.MaterialDelete.MaterialDeleteLayout;

import java.util.Collections;
import java.util.List;

/**
 * Comment and decouple the item listener and adapter
 * Solved the conflicts between the Item onTouch and onClick
 *
 * @param <T>
 */

public class ItemRvCategoryAdapter<T> extends RecyclerView.Adapter<ItemRvCategoryAdapter<T>.ViewHolder> {
    private List<T> items = Collections.emptyList();
    Activity mActivity;

    @Override
    public ItemRvCategoryAdapter<T>.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delete_list_cardview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemRvCategoryAdapter<T>.ViewHolder holder, int position) {
        final ItemCategory category = (ItemCategory) items.get(position);
        holder.mName.setText(category.getCategoryName());
        holder.mNum.setText(category.getMoneyNum());
        if (category.getColor() == 0) {
            holder.mColor.setBackgroundColor(mActivity.getResources().getColor(R.color.lightRed));
            holder.mNum.setTextColor(mActivity.getResources().getColor(R.color.lightRed));
        } else {
            holder.mColor.setBackgroundColor(mActivity.getResources().getColor(R.color.forestGreen));
            holder.mNum.setTextColor(mActivity.getResources().getColor(R.color.forestGreen));
        }
        holder.mPic.setImageResource(category.getCategoryPic());
        holder.layout.setDeleteListener(new MaterialDeleteLayout.SwipeDeleteListener() {
            @Override
            public void onDelete() {
                int pos = (int) holder.layout.getTag();
                notifyItemRemoved(pos);
                items.remove(pos);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.layout.closeItem();
                        notifyDataSetChanged();
                    }
                }, 1000);

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView mPic;
        public TextView mName;
        public TextView mNum;
        public ImageView mColor;
        public LinearLayout mContainer;
        public ImageView mDelete;
        public MaterialDeleteLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            mNum = (TextView) itemView.findViewById(R.id.category_num);
            mName = (TextView) itemView.findViewById(R.id.category_name);
            mColor = (ImageView) itemView.findViewById(R.id.category_color);
            mPic = (ImageView) itemView.findViewById(R.id.category_icon);
            mContainer = (LinearLayout) itemView.findViewById(R.id.ll_container);
            mDelete = (ImageView) itemView.findViewById(R.id.iv_delete);
            layout = (MaterialDeleteLayout) itemView.findViewById(R.id.item_delete_layout);
        }
    }
}
