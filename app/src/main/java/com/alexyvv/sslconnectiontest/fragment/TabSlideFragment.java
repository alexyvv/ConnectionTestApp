package com.alexyvv.sslconnectiontest.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexyvv.sslconnectiontest.R;
import com.alexyvv.sslconnectiontest.adapter.PageFragmentAdapter;
import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexy on 08.04.15.
 */
public class TabSlideFragment extends Fragment {

    private View mFragmentView;
    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager mViewPager;
    private List<String> titleNameList;

    public static TabSlideFragment newInstance() {

        TabSlideFragment tabsSlideFragment = new TabSlideFragment();
        return tabsSlideFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.mFragmentView = inflater.inflate(R.layout.tab_slide_fragment_layout, container, false);

        loadViewComponents();

        return this.mFragmentView;
    }

    private void loadViewComponents() {

        this.mViewPager = (ViewPager) mFragmentView.findViewById(R.id.view_pager);
        titleNameList = new ArrayList<>(4);
        titleNameList.add("Connection");
        titleNameList.add("HTTP");
        titleNameList.add("HTTPS");
        titleNameList.add("info");
        this.mViewPager.setAdapter(new PageFragmentAdapter(titleNameList, getChildFragmentManager()));
        this.mPagerSlidingTabStrip = (PagerSlidingTabStrip) mFragmentView.findViewById(R.id.pager_sliding_tab);
        this.mPagerSlidingTabStrip.setViewPager(this.mViewPager);
        this.mPagerSlidingTabStrip.setTextColor(getResources().getColor(R.color.text));
        this.mPagerSlidingTabStrip.setIndicatorColor(getResources().getColor(R.color.dividerColor));
    }

    /**
     * Задать активный таб.
     * @param aTabNumber номер активного таба.
     */
    public void setSelectetTab(int aTabNumber) {

        mViewPager.setCurrentItem(aTabNumber, true);
    }
}
