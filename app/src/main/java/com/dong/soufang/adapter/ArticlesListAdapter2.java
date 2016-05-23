package com.dong.soufang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
 * Date: 16/3/22
 */
public class ArticlesListAdapter2 extends BaseLoadMoreRecyclerAdapter<String, ArticlesListAdapter2.NewsListViewHolder> {

    private Context context;
    private List<Article> articleList;
    private OnListItemClickListener listener;

    public ArticlesListAdapter2(Context context, List<Article> list) {
        this.context = context;
        this.articleList = list;
    }

    public void setOnListItemClickListener(OnListItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public NewsListViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_articles_list, null);
        return new NewsListViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(final NewsListViewHolder holder, final int position) {
        Article article = articleList.get(position);
        holder.tvTitle.setText(article.getTitle());
        holder.tvSubTitle.setText(article.getSubTitle());
        holder.tvDate.setText(article.getCreatedAt());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(holder.itemView, position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null) {
                    listener.onLongClick(holder.itemView, position);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (articleList != null) {
            return articleList.size();
        }
        return 0;
    }

    class NewsListViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvSubTitle, tvDate;
        ImageView ivAvatar;

        public NewsListViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvSubTitle = (TextView) itemView.findViewById(R.id.tv_sub_title);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
        }
    }
}
