package com.dong.soufang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dong.soufang.R;
import com.dong.soufang.activity.ArticleDetailActivityV2;
import com.dong.soufang.activity.base.BaseActivity;
import com.dong.soufang.adapter.ArticlesListAdapter1;
import com.dong.soufang.bean.Article;
import com.dong.soufang.custom.recyclerView.LoadMoreRecyclerView;
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
 * <p/>
 * Author: dong
 * Date: 16/3/15
 */
public class ArticlesFragment extends BaseFragment implements OnListItemClickListener,
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private static final String TAG = ArticlesFragment.class.getSimpleName();
    private List<Article> articleList;
    private LoadMoreRecyclerView recyclerView;
    private ArticlesListAdapter1 mAdapter;
    private int mPage = 1;
    private SwipeRefreshLayout swipeRefreshLayout;

    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_articles, container, false);
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        articleList = new ArrayList<>();
        recyclerView = (LoadMoreRecyclerView) contentView.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        fab = (FloatingActionButton) contentView.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAutoLoadMoreEnable(true);
        recyclerView.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                getArticlesList();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (((LoadMoreRecyclerView) recyclerView).getFirstVisiblePosition() == 0) {
                    fab.setVisibility(View.GONE);
                } else {
                    fab.setVisibility(View.VISIBLE);
                }
            }
        });

        getArticlesList();
    }

    /**
     * 请求新闻列表
     */
    private void getArticlesList() {
        swipeRefreshLayout.setRefreshing(false);

        HashMap<String, Object> params = new HashMap<>();
        params.put("page", TextUtils.isEmpty(mPage + "") ? 1 : mPage++);
        RequestBean requestBean = new RequestBean(TAG, HttpApi.GetArticlesListApi, params);
        ((BaseActivity) getActivity()).httpHandler.getArticlesList(requestBean, new Callback() {
            @Override
            public void onSuccess(BaseResult result) {
                List<Article> articleListT = (List<Article>) result.getData();
                if (articleListT.size() > 0) {
                    articleList.addAll(articleListT);
                    if (mAdapter == null) {
                        mAdapter = new ArticlesListAdapter1(context, articleList);
                        recyclerView.setAdapter(mAdapter);
                    } else {
                        recyclerView.notifyMoreFinish(true);
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    recyclerView.notifyMoreFinish(false);
                }
                mAdapter.setOnListItemClickListener(ArticlesFragment.this);
            }

            @Override
            public void onFailure(String msg) {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        getArticlesList();
    }

    @Override
    public void onClick(View itemView, int position) {
        Intent intent = new Intent(context, ArticleDetailActivityV2.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", articleList.get(position).getId());
        bundle.putString("avatar", articleList.get(position).getAvatar());
        bundle.putBoolean("is_collected", articleList.get(position).isCollected());
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void onLongClick(View itemView, int position) {
        Toast.makeText(context, "long click" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                if (recyclerView != null) {
                    recyclerView.smoothScrollToPosition(0);
                }
                break;
        }
    }
}
