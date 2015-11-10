
package com.sayagodshala.livesplash.fragments.category;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sayagodshala.livesplash.BaseFragment;
import com.sayagodshala.livesplash.R;

import java.util.ArrayList;
import java.util.List;

public class CategorySlidingTabsFragment extends BaseFragment {

    public static final String OUTLET = "outlet";
    public static final String CATEGORY_ID = "categoryId";
    static final String LOG_TAG = "SlidingTabsColorsFragment";
    private CategorySlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    private List<CategoryTabsItem> mTabs = new ArrayList<CategoryTabsItem>();

    private Bundle args;
    private int categoryId = 0;
    private String categoryCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // BEGIN_INCLUDE (populate_tabs)
        /**
         * Populate our tab list with tabs. Each item contains a title, indicator color and divider
         * color, which are used by {@link CategorySlidingTabLayout}.
         */

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_category_pager_tabs, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        for (int i = 0; i < 3; i++) {
            mTabs.add(new CategoryTabsItem("Tab " + (i + 1)));
        }

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new CategoryFragmentPagerAdapter(getChildFragmentManager(), mTabs));
        mViewPager.setCurrentItem(categoryId);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mSlidingTabLayout = (CategorySlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
        mSlidingTabLayout.setCustomTabColorizer(new CategorySlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return mTabs.get(position).getIndicatorColor();
            }

            @Override
            public int getDividerColor(int position) {
                return mTabs.get(position).getDividerColor();
            }
        });
    }

    /*private void defaultConfigurations() {

    }*/


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}