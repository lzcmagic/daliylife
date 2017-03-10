package com.example.lzc.daliylife.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lzc.daliylife.R;
import com.example.lzc.daliylife.entity.MovieEntity;
import com.example.lzc.daliylife.fragments.DaliyEventsFragment;
import com.example.lzc.daliylife.fragments.NewsFragment;
import com.example.lzc.daliylife.fragments.WeChartFragment;
import com.example.lzc.daliylife.framework.Constants;
import com.example.lzc.daliylife.utillistener.ToolbarTitleChange;
import com.example.lzc.daliylife.utils.HttpMethods;
import com.example.lzc.daliylife.utils.ProgressSubscriber;
import com.example.lzc.daliylife.utils.SubscriberOnNextListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.frame_cont)
    FrameLayout container;
    @BindView(R.id.floatBtn)
    FloatingActionButton floatButton;
    private String CurrentWeather;
    private String CurrentWeatherText;
    /**
     * toolbar标题更改
     */
    private ToolbarTitleChange mTitleListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        CurrentWeather = getIntent().getStringExtra("weather");
        CurrentWeatherText = getIntent().getStringExtra("weatherText");
        toolbar.setTitle(getResources().getString(R.string.toolbar_news));
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RecyclerViewTest.class));
            }
        });
        mTitleListener = new ToolbarTitleChange() {
            @Override
            public void TitleChange(String title) {
                toolbar.setTitle(title);
            }
        };
        navView.setNavigationItemSelectedListener(this);
        ColorStateList stateList = getResources().getColorStateList(R.color.navigation_menu_item_color);
        navView.setItemIconTintList(stateList);
        navView.setItemTextColor(stateList);
        navView.getMenu().getItem(0).setChecked(true);

        initNavHeadView();
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().replace(R.id.frame_cont, new NewsFragment());
            transaction.commit();
        }
    }

    private void initNavHeadView() {
        View headerView = navView.getHeaderView(0);
        ImageView image = (ImageView) headerView.findViewById(R.id.imageView);
        TextView weather = (TextView) headerView.findViewById(R.id.weather);
        TextView weatherText = (TextView) headerView.findViewById(R.id.weather_text);
        weather.setText(CurrentWeather);
        weatherText.setText(CurrentWeatherText);
    }

    /**
     * intent
     *
     * @param context
     */
    public static void actionStart(Context context, String weather, String weatherText) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("weather", weather);
        intent.putExtra("weatherText", weatherText);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentTransaction mTransaction =
                getSupportFragmentManager().beginTransaction();
        int id = item.getItemId();
        if (id == R.id.news) {
            mTransaction.replace(R.id.frame_cont, new NewsFragment(), Constants.FragmentTagNews);
            mTitleListener.TitleChange(getResources().getString(R.string.toolbar_news));
        } else if (id == R.id.wechart) {
            mTransaction.replace(R.id.frame_cont, new WeChartFragment(), Constants.FragmentTagWeChart);
            mTitleListener.TitleChange(getResources().getString(R.string.toolbar_wechart));
        } else if (id == R.id.car) {
            mTransaction.replace(R.id.frame_cont, new NewsFragment(), Constants.FragmentTagNews);
            mTitleListener.TitleChange(getResources().getString(R.string.toolbar_car));
        } else if (id == R.id.daliy) {
            mTransaction.replace(R.id.frame_cont, new DaliyEventsFragment(), Constants.FragmentTagDaliy);
            mTitleListener.TitleChange(getResources().getString(R.string.toolbar_daliy));
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_about) {

        } else {

        }
        mTransaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void getMovie() {
        HttpMethods.getInstance(Constants.DOUBANAPI).getTopMovie(new ProgressSubscriber<MovieEntity>(new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                if (o instanceof MovieEntity) {
                    Log.d(Constants.NORMALTAG, ((MovieEntity) o).toString());
                    // resultTV.setText(((MovieEntity) o).toString());
                }
            }
        }), 0, 10);
    }

}
