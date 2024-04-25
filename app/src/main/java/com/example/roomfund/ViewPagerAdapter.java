package com.example.roomfund;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.roomfund.fragment.history;
import com.example.roomfund.fragment.home;
import com.example.roomfund.fragment.myProfile;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new home();
            case 1:
                return new history();
            default:
                return new myProfile();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
