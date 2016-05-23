package com.dong.soufang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.dong.soufang.GlobalData;
import com.dong.soufang.MainApplication;
import com.dong.soufang.R;
import com.dong.soufang.activity.base.BaseActivity;
import com.dong.soufang.adapter.MainTabAdapter;
import com.dong.soufang.bean.User;
import com.dong.soufang.custom.SelectableRoundedImageView;
import com.dong.soufang.fragment.ArticlesFragment;
import com.dong.soufang.fragment.EstatesFragment;
import com.dong.soufang.http.BaseResult;
import com.dong.soufang.http.Callback;
import com.dong.soufang.http.HttpApi;
import com.dong.soufang.http.RequestBean;
import com.dong.soufang.util.map.CityHashMap;
import com.dong.soufang.util.ImageLoaderUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private MainApplication application;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<Fragment> fragments;
    private Fragment estatesFragment, newsFragment;
    private MainTabAdapter mAdapter;
    private NavigationView navigationView;
    private TextView tvName, tvMobile, tvAddress;
    private SelectableRoundedImageView ivAvatar;

    private MenuItem cityBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        application = MainApplication.getInstance();

        initToolbar();
        initLocation();
        initData();

    }

    private void initLocation() {

    }

    @Override
    protected void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initData() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);

        //设置TabLayout
        fragments = new ArrayList<>();
        estatesFragment = new EstatesFragment();
        newsFragment = new ArticlesFragment();
        fragments.add(estatesFragment);
        fragments.add(newsFragment);

        List<String> titles = new ArrayList<>();
        titles.add("房产");
        titles.add("信息");

        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));
        mAdapter = new MainTabAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        //设置Drawer相关
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tvName = (TextView) navigationView.findViewById(R.id.tv_estate_name);
        tvMobile = (TextView) navigationView.findViewById(R.id.tv_mobile);
        tvAddress = (TextView) navigationView.findViewById(R.id.tv_address);
        ivAvatar = (SelectableRoundedImageView) navigationView.findViewById(R.id.iv_avatar);
    }

    /**
     * 获取用户信息
     */
    private void getPersonInfo() {
        HashMap<String, Object> params = new HashMap<>();
        RequestBean requestBean = new RequestBean(TAG, HttpApi.GetPersonalInfoApi, params);
        httpHandler.getPersonalInfo(requestBean, new Callback() {
            @Override
            public void onSuccess(BaseResult result) {
                GlobalData.USER = (User) result.getData();
                ImageLoaderUtils.display(GlobalData.USER.getAvatar(), ivAvatar, R.mipmap.ic_launcher);
                tvName.setText(GlobalData.USER.getName());
                tvMobile.setText(GlobalData.USER.getMobile());

            }

            @Override
            public void onFailure(String msg) {
                showToast(msg);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!GlobalData.isExit) {
            GlobalData.isExit = true;
            showToast("再按一次退出程序");
            Message msg = new Message();
            msg.what = 0;
            handler.sendMessageDelayed(msg, 2000);
        } else {
            application.exitApplication();
        }

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    GlobalData.isExit = false;
                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        cityBtn = toolbar.getMenu().findItem(R.id.action_location);
        String city = CityHashMap.cityMap.get("021");
        cityBtn.setTitle(TextUtils.isEmpty(city) ? "未知城市" : city);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_location:
                Intent intent = new Intent(MainActivity.this, SelectCityActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.action_settings:
                showToast("设置, 待实现");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_collection:
                startActivity(new Intent(MainActivity.this, MyCollectionActivity.class));
                break;
            case R.id.nav_gallery:
                startActivity(new Intent(MainActivity.this, MyAccountActivity.class));
                break;
            case R.id.nav_slideshow:
                startActivity(new Intent(MainActivity.this, MyMessageActivity.class));
                break;
            case R.id.nav_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            default:
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 100:
                //修改当前城市
                GlobalData.CITY = data.getStringExtra("city");
                HttpApi.CITY = TextUtils.isEmpty(CityHashMap.cityMap.get(GlobalData.CITY)) ?
                        HttpApi.CITY : GlobalData.CITY + "/";
                String city = CityHashMap.cityMap.get(GlobalData.CITY);
                cityBtn.setTitle(TextUtils.isEmpty(city) ? "未知城市" : city);
                // TODO 刷新数据
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (MainApplication.getInstance().locationClient.isStarted()) {
            MainApplication.getInstance().locationClient.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        MainApplication.getInstance().locationClient.stop();
    }

}
