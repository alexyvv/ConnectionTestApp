package com.alexyvv.sslconnectiontest.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.alexyvv.sslconnectiontest.fragment.ConnectionFragment;

import java.util.List;

/**
 * Created by alexy on 08.04.15.
 */
public class PageFragmentAdapter extends FragmentPagerAdapter {

    private List<String> mListTitleTabs;

    public PageFragmentAdapter(List<String> listTitleTabs, FragmentManager childFragmentManager) {

        super(childFragmentManager);
        this.mListTitleTabs = listTitleTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if (mListTitleTabs == null || mListTitleTabs.isEmpty()) {
            return "";
        }
        return mListTitleTabs.get(position);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                // Фрагмент для проверки соединения.
                return ConnectionFragment.newInstance();
            case 2:
                //  Фрагмент для выполнения HTTP запросов.
                return ConnectionFragment.newInstance();
            case 3:
                return ConnectionFragment.newInstance();
            case 4:
                return ConnectionFragment.newInstance();
            default:
                return ConnectionFragment.newInstance();
        }
    }

    @Override
    public int getCount() {

        if (mListTitleTabs == null || mListTitleTabs.isEmpty()) {
            return 0;
        }
        return mListTitleTabs.size();
    }
}
