package com.example.rohangoyal2014.agrokart;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {


    public RegisterFragment() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_register, container, false);

        final MainActivity mainActivity=(MainActivity)getActivity();

        mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        mainActivity.getSupportActionBar().setTitle(getString(R.string.register));

        return view;
    }

}
