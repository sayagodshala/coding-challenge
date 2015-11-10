package com.sayagodshala.livesplash.fragments.category;

import android.graphics.Color;
import android.support.v4.app.Fragment;


public class CategoryTabsItem {
    private final CharSequence mTitle;

    CategoryTabsItem(CharSequence title) {
        mTitle = title;
    }

    /**
     * @return A new {@link Fragment} to be displayed by a {@link android.support.v4.view.ViewPager}
     */
    Fragment createFragment(int categoryId) {
        return CategoryContentFragment.newInstance(mTitle, categoryId);
    }

    /**
     * @return the title which represents this tab. In this sample this is used directly by
     * {@link android.support.v4.view.PagerAdapter#getPageTitle(int)}
     */
    CharSequence getTitle() {
        return mTitle;
    }


    /**
     * @return the color to be used for indicator on the {@link CategorySlidingTabLayout}
     */
    int getIndicatorColor() {
//        return mIndicatorColor;
        return Color.WHITE;
    }

    /**
     * @return the color to be used for right divider on the {@link CategorySlidingTabLayout}
     */
    int getDividerColor() {
//        return mDividerColor;
        return Color.TRANSPARENT;
    }
}