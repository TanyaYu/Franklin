package com.tanyayuferova.franklin.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tanyayuferova.franklin.FranklinApplication;
import com.tanyayuferova.franklin.databinding.FragmentOnboardingBinding;

import ru.terrakok.cicerone.Router;

/**
 * Created by Tanya Yuferova on 3/18/2018.
 */

public class OnBoardingFragment extends  Fragment {

    private FragmentOnboardingBinding binding;
    final public static String SCREEN_KEY = "ON_BOARDING_FRAGMENT_SCREEN_KEY";

    private Router router = FranklinApplication.INSTANCE.getRouter();

    public static OnBoardingFragment newInstance(Object data) {
        OnBoardingFragment fragment = new OnBoardingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false);

        binding.skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                router.exit();
            }
        });
        binding.exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                router.exit();
            }
        });
        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.viewSwitcher.showNext();
            }
        });
        binding.viewSwitcher.setInAnimation(getContext(), android.R.anim.slide_in_left);
        binding.viewSwitcher.setOutAnimation(getContext(), android.R.anim.slide_out_right);
        return binding.getRoot();
    }
}
