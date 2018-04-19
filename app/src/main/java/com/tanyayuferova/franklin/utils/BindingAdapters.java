package com.tanyayuferova.franklin.utils;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

/**
 * Created by Tanya Yuferova on 4/19/2018.
 */

public class BindingAdapters {

    @BindingAdapter("app:src")
    public static void setDrawableResource(ImageView imageView, @DrawableRes int res){
        Drawable drawable = imageView.getResources().getDrawable(res);
        imageView.setImageDrawable(drawable);
    }
}
