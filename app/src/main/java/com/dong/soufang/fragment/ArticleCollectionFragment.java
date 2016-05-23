package com.dong.soufang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dong.soufang.R;
import com.dong.soufang.activity.base.BaseActivity;
import com.dong.soufang.adapter.ArticlesListAdapter;
import com.dong.soufang.bean.Article;
import com.dong.soufang.http.BaseResult;
import com.dong.soufang.http.Callback;
import com.dong.soufang.http.HttpApi;
import com.dong.soufang.http.RequestBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Description: 文章收藏Fragment
 * <p/>
 * Author: dong
 * Date: 16/3/29
 */
public class ArticleCollectionFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = ArticleCollectionFragment.class.getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ArticlesListAdapter mAdapter;
    private List<Article> articleList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_articles, container, false);
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) contentView.findViewById(R.id.recycler_view);
        articleList = new ArrayList<>();

        getArticlesCollection();

    }

    private void getArticlesCollection() {
        swipeRefreshLayout.setRefreshing(false);

        HashMap<String, Object> params = new HashMap<>();
        RequestBean requestBean = new RequestBean(TAG, HttpApi.GetUserArticleCollectionsApi, params);
        ((BaseActivity) getActivity()).httpHandler.getUserArticlesCollection(requestBean, new Callback() {
            @Override
            public void onSuccess(BaseResult result) {
                articleList = (List<Article>) result.getData();
                if (mAdapter == null) {
                    recyclerView.setAdapter(mAdapter = new ArticlesListAdapter(context, articleList));
                } else {
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String msg) {
                showToast(msg);
            }
        });
    }

    @Override
    public void onRefresh() {
        getArticlesCollection();
    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
