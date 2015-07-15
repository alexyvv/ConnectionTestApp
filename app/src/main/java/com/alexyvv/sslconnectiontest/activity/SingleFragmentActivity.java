package com.alexyvv.sslconnectiontest.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.alexyvv.sslconnectiontest.R;
import com.alexyvv.sslconnectiontest.fragment.TabSlideFragment;
import com.alexyvv.sslconnectiontest.listener.DrawerItemClickListener;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;

/**
 * Created by alexy on 07.07.15.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    /** Кастомный Drawer компонент. */
    private Drawer mCustomDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_fragment_activity);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);


        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SingleFragmentActivity.this.finish();
            }
        });
        FragmentManager fm = getSupportFragmentManager();
        Fragment activityFragment = fm.findFragmentById(R.id.fragment_container);
        if(activityFragment == null) {
            activityFragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, activityFragment)
                    .commit();
        }

        // Drawer (custom implementation)
        DrawerBuilder drawerBuilder = new DrawerBuilder(this);
        mCustomDrawer = drawerBuilder
//                .withRootView(R.id.drawer_container)
                .withToolbar(mToolBar)
                .withHeader(R.layout.drawer_header)
                .withTranslucentStatusBar(true)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.title_connection).withIcon(FontAwesome.Icon.faw_plug),
                        new PrimaryDrawerItem().withName(R.string.title_http).withIcon(FontAwesome.Icon.faw_rocket),
                        new PrimaryDrawerItem().withName(R.string.title_https).withIcon(FontAwesome.Icon.faw_lock),
                        new PrimaryDrawerItem().withName(R.string.title_connection_info).withIcon(FontAwesome.Icon.faw_info),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.title_app_info).withIcon(FontAwesome.Icon.faw_info_circle)

                )
                .withOnDrawerItemClickListener(new DrawerItemClickListener(this))
                .withSavedInstance(savedInstanceState)
                .build();
    }

    /**
     * Получить объект компонента Drawer.
     * @return объект компонента Drawer.
     */
    public Drawer getmCustomDrawer(){

        return this.mCustomDrawer;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    public void onBackPressed() {

        if(mCustomDrawer != null && mCustomDrawer.isDrawerOpen()) {
            mCustomDrawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Создание фрагмента.
     * @return единственный фрагмент активности.
     */
    protected abstract Fragment createFragment();
}

