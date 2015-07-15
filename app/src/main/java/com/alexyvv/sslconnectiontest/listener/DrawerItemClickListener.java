package com.alexyvv.sslconnectiontest.listener;


import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.alexyvv.sslconnectiontest.R;
import com.alexyvv.sslconnectiontest.activity.SingleFragmentActivity;
import com.alexyvv.sslconnectiontest.fragment.TabSlideFragment;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

/**
 * Слушатель Drawer компонента.
 */
public class DrawerItemClickListener implements Drawer.OnDrawerItemClickListener {

    private static final String TAG = "DrawerItemClickListener";

    private SingleFragmentActivity contextActivity;

    /**
     * Конструктор слушателя Drawer компонента.
     * @param aContextActivity активность в которой создается Drawer компонент.
     */
    public DrawerItemClickListener(AppCompatActivity aContextActivity) {

        this.contextActivity = (SingleFragmentActivity) aContextActivity;
    }

    @Override
    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {

        Log.d(TAG, "Drawer item click: id = " + id + ", position =" + position);
        actionSelectDrawerItem(position);
        return true;
    }

    private void actionSelectDrawerItem(int position){

        Drawer drawer = contextActivity.getmCustomDrawer();
        if(drawer != null) {
            drawer.closeDrawer();
        }
        // Перемещаемся на нужный фрагмент.
        TabSlideFragment tabSlideFragment = (TabSlideFragment) contextActivity.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if(tabSlideFragment != null && tabSlideFragment.isAdded()) {
            tabSlideFragment.setSelectetTab(position);
        }
    }
}
