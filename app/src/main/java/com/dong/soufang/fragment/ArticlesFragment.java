package com.dong.soufang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dong.soufang.R;
import com.dong.soufang.activity.ArticleDetailActivity;
import com.dong.soufang.activity.base.BaseActivity;
import com.dong.soufang.adapter.ArticlesListAdapter2;
import com.dong.soufang.bean.Article;
import com.dong.soufang.http.BaseResult;
import com.dong.soufang.http.Callback;
import com.dong.soufang.http.HttpApi;
import com.dong.soufang.http.RequestBean;
import com.dong.soufang.impl.OnListItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Description: Home新闻列表模块
 * <p>
 * Author: dong
 * Date: 16/3/15
 */
public class ArticlesFragment extends BaseFragment implements OnListItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = ArticlesFragment.class.getSimpleName();
    private List<Article> articleList;
    private RecyclerView recyclerView;
    private ArticlesListAdapter2 mAdapter;
    private String nextStart = "0";
    private SwipeRefreshLayout swipeRefreshLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_articles, container, false);
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        articleList = new ArrayList<>();
        recyclerView = (RecyclerView) contentView.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        getArticlesList();
    }

    /**
     * 请求新闻列表
     */
    private void getArticlesList() {
        swipeRefreshLayout.setRefreshing(false);

        HashMap<String, Object> params = new HashMap<>();
        params.put("next_start", TextUtils.isEmpty(nextStart) ? "0" : nextStart);
        RequestBean requestBean = new RequestBean(TAG, HttpApi.GetArticlesListApi, params);
        ((BaseActivity) getActivity()).httpHandler.getArticlesList(requestBean, new Callback() {
            @Override
            public void onSuccess(BaseResult result) {
                articleList = (List<Article>) result.getData();
                if (articleList != null) {
                    if (mAdapter == null) {
                        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                        recyclerView.setLayoutManager(layoutManager);
                        mAdapter = new ArticlesListAdapter2(context, articleList);
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.setOnListItemClickListener(ArticlesFragment.this);
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onFailure(String msg) {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                //TODO
            }
        });
    }

    @Override
    public void onRefresh() {
        getArticlesList();
    }

    @Override
    public void onClick(View itemView, int position) {
        Intent intent = new Intent(context, ArticleDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", articleList.get(position).getId());
        bundle.putBoolean("is_collected", articleList.get(position).isCollected());
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void onLongClick(View itemView, int position) {
        Toast.makeText(context, "long click" + position, Toast.LENGTH_SHORT).show();
    }
}
