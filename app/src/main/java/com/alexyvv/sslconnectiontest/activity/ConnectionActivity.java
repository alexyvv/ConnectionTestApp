package com.alexyvv.sslconnectiontest.activity;

import android.support.v4.app.Fragment;

import com.alexyvv.sslconnectiontest.fragment.ConnectionFragment;
import com.alexyvv.sslconnectiontest.fragment.TabSlideFragment;

public class ConnectionActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {

        return TabSlideFragment.newInstance();
    }
}
