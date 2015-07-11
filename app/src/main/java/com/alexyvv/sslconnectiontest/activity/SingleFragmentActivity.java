package com.alexyvv.sslconnectiontest.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.alexyvv.sslconnectiontest.R;
import com.alexyvv.sslconnectiontest.fragment.NavigationDrawerFragment;

/**
 * Created by alexy on 07.07.15.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    private Toolbar mToolBar;

    /** Fragment managing the behaviors, interactions and presentation of the navigation drawer. */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_fragment_activity);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("");

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

        // Drawer
        // Фрагмент со сдвигающейся панелью:
        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer_fragment);
        // Лейаут сдвигающейся панели:
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationDrawerFragment.setUp(drawerLayout, mToolBar);
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

    /**
     * Создание фрагмента.
     * @return единственный фрагмент активности.
     */
    protected abstract Fragment createFragment();
}
