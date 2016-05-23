package com.dong.soufang.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.dong.soufang.R;
import com.dong.soufang.activity.base.BaseActivity;
import com.dong.soufang.fragment.ArticleCollectionFragment;
import com.dong.soufang.fragment.HouseCollectionFragment;

/**
 * Description: 我的收藏
 * <p/>
 * Author: dong
 * Date: 16/3/20
 */
public class MyCollectionActivity extends BaseActivity {
    private static final String TAG = MyCollectionActivity.class.getSimpleName();
    private Fragment articleCollectionFragment, houseCollectionFragment;
//    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);

        initToolbar();

        houseCollectionFragment = new HouseCollectionFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.content_layout, houseCollectionFragment).commit();
    }

    @Override
    protected void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_collection, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()) {
            case R.id.estates:
                if (houseCollectionFragment == null) {
                    houseCollectionFragment = new HouseCollectionFragment();
                    transaction.replace(R.id.content_layout, houseCollectionFragment);
                } else {
                    transaction.replace(R.id.content_layout, houseCollectionFragment);
                }
                transaction.commit();
                break;
            case R.id.articles:
                if (articleCollectionFragment == null) {
                    articleCollectionFragment = new ArticleCollectionFragment();
                    transaction.replace(R.id.content_layout, articleCollectionFragment);
                } else {
                    transaction.replace(R.id.content_layout, articleCollectionFragment);
                }
                transaction.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
