package com.sayagodshala.livesplash.fragments.category;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.List;

public class CategoryFragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private List<CategoryTabsItem> mTabs;

    CategoryFragmentPagerAdapter(FragmentManager fm, List<CategoryTabsItem> tabs) {
        super(fm);
        mTabs = tabs;
    }

    @Override
    public Fragment getItem(int i) {
        return mTabs.get(i).createFragment(i);
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs.get(position).getTitle();
    }

}