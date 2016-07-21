package com.dong.soufang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dong.soufang.R;
import com.dong.soufang.bean.Article;
import com.dong.soufang.impl.OnListItemClickListener;

import java.util.List;

/**
 * Description:
 * <p>
 * Author: dong
 * Date: 7/21/16
 */
public class ArticlesListAdapter1 extends RecyclerView.Adapter<ArticlesListAdapter1.ArticlesListViewHolder> {
    private Context context;
    private List<Article> articleList;
    private OnListItemClickListener mListener;

    public ArticlesListAdapter1(Context context, List<Article> list) {
        this.context = context;
        this.articleList = list;
    }

    public void setOnListItemClickListener(OnListItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public ArticlesListAdapter1.ArticlesListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_articles_list, parent, false);
        return new ArticlesListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ArticlesListViewHolder holder, final int position) {
        Article article = articleList.get(position);
        holder.tvTitle.setText(article.getTitle());
        holder.tvSubTitle.setText(article.getSubTitle());
        holder.tvDate.setText(article.getCreatedAt());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick(holder.itemView, position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mListener != null) {
                    mListener.onLongClick(holder.itemView, position);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    class ArticlesListViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvSubTitle, tvDate;
        ImageView ivAvatar;

        public ArticlesListViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvSubTitle = (TextView) itemView.findViewById(R.id.tv_sub_title);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
        }
    }
}
