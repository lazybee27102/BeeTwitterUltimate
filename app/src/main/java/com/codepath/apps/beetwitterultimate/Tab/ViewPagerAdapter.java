package com.codepath.apps.beetwitterultimate.Tab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by beeiscoding on 01/04/2016.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private final ArrayList<Fragment> fragmentsList;
    private final ArrayList<String> fragmentTitle;



    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentsList = new ArrayList<>();
        fragmentTitle = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }

    public void addFragment(Fragment fragment,String title)
    {
        if(!fragmentsList.contains(fragment) && !fragmentTitle.contains(title))
        {
            fragmentsList.add(fragment);
            fragmentTitle.add(title);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitle.get(position);
    }
}
