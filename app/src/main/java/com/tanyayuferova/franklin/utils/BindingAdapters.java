package com.tanyayuferova.franklin.utils;

import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Tanya Yuferova on 4/19/2018.
 */

public class BindingAdapters {

    @BindingAdapter("app:src")
    public static void setDrawableResource(ImageView view, @DrawableRes int res){
        Drawable drawable;
        try {
            drawable = view.getResources().getDrawable(res);
        } catch (Resources.NotFoundException exception) {
            drawable = null;
        }
        view.setImageDrawable(drawable);
    }

    @BindingAdapter("app:background")
    public static void setBackgroundColorFromResource(View view, @ColorRes int res){
        view.setBackgroundColor(ContextCompat.getColor(view.getContext(), res));
    }

    @BindingAdapter("app:textColor")
    public static void setTextColorFromResource(TextView view, @ColorRes int res){
        view.setTextColor(ContextCompat.getColor(view.getContext(), res));
    }
}
