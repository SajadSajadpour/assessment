package com.example.assessment.Adapter;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.assessment.MainFragment;
import com.example.assessment.Util.Constants;

public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.Queries.queryLabel, position);
        MainFragment fragment = new MainFragment();
        fragment.setArguments(bundle);
        Log.d(this.getClass().getSimpleName(), "queryType: " + String.valueOf(position));
        return fragment;
    }

    @Override
    public int getCount() {
        return Constants.Tabs.TABS_COUNT;
    }
}
