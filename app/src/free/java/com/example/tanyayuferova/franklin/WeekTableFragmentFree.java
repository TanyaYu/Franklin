package com.example.tanyayuferova.franklin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.tanyayuferova.franklin.entity.Virtue;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.Date;

/**
 * Created by Tanya Yuferova on 12/26/2017.
 */

public class WeekTableFragmentFree extends WeekTableFragment {

    private NativeExpressAdView nativeExpressAdView;

    public WeekTableFragmentFree() {
    }

    public static WeekTableFragmentFree newInstance(Date startDate, Virtue virtue) {
        WeekTableFragmentFree fragment = new WeekTableFragmentFree();
        fragment.setArguments(new Bundle());
        fragment.getArguments().putInt(ARGUMENT_LOADER_ID, ++countLoaders);
        fragment.getArguments().putLong(ARGUMENT_START_DATE, startDate.getTime());
        fragment.getArguments().putParcelable(ARGUMENT_VIRTUE_OF_WEEK, virtue);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        nativeExpressAdView = new NativeExpressAdView(getContext());
        nativeExpressAdView.setAdSize(AdSize.BANNER);
        nativeExpressAdView.setAdUnitId(getContext().getResources().getString(R.string.ad_unit_id));

        AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
        ((FrameLayout) view.findViewById(R.id.ad_content)).addView(nativeExpressAdView);
        nativeExpressAdView.loadAd(adRequestBuilder.build());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (nativeExpressAdView != null)
            nativeExpressAdView.resume();
    }

    @Override
    public void onPause() {
        if (nativeExpressAdView != null)
            nativeExpressAdView.pause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (nativeExpressAdView != null)
            nativeExpressAdView.destroy();
        super.onDestroy();
    }
}
