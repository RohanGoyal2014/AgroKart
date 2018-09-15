package com.example.rohangoyal2014.agrokart;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class LoginFragmentAdapter extends FragmentPagerAdapter{

    public LoginFragmentAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1:
                return new RegisterFragment();
            default:
                return new LoginFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
