package com.tanyayuferova.franklin.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tanyayuferova.franklin.databinding.FragmentDaysOfWeekBinding;

import java.util.Date;

/**
 * Created by Tanya Yuferova on 3/4/2018.
 */

public class DaysOfWeekFragment extends Fragment {

    private FragmentDaysOfWeekBinding binding;

    public static final String FIRST_DATE = "FIRST_DATE";

    public DaysOfWeekFragment() {
    }

    public static DaysOfWeekFragment newInstance(Date firstDate) {
        DaysOfWeekFragment fragment = new DaysOfWeekFragment();
        Bundle args = new Bundle();
        args.putLong(FIRST_DATE, firstDate.getTime());
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (!getArguments().containsKey(FIRST_DATE))
            throw new UnsupportedOperationException("DaysOfWeekFragment must have first date in the arguments");

        if(!(getParentFragment() instanceof DaySelectorWidget.OnDayClickedListener))
            throw new UnsupportedOperationException("Activity must implement OnDayClickedListener");

        binding = FragmentDaysOfWeekBinding.inflate(inflater, container, false);
        binding.daysWidget.setFirstDate(new Date(getArguments().getLong(FIRST_DATE)));
        binding.daysWidget.setOnDayClickedListener((DaySelectorWidget.OnDayClickedListener) getParentFragment());

        //fixme
        if(getParentFragment() instanceof VirtuesFragment) {
            Date date = ((VirtuesFragment) getParentFragment()).getCurrentDate();
            binding.daysWidget.selectDate(date);
        }

        return binding.getRoot();
    }
}