package com.tanyayuferova.franklin.entity.viewModel;

import android.content.Context;

import com.tanyayuferova.franklin.R;
import com.tanyayuferova.franklin.entity.Virtue;

/**
 * Created by Tanya Yuferova on 3/4/2018.
 */

public class VirtueDayInfo {

    private Virtue virtue;
    private String marks;

    public VirtueDayInfo() {}

    public VirtueDayInfo(Virtue virtue) {
        this.virtue = virtue;
    }

    public VirtueDayInfo(Virtue virtue, int dotsCount, Context context) {
        this(virtue);
        marks = dotsDescription(context, dotsCount);
    }

    private String dotsDescription(Context context, int dotsCount) {
        String text = "";
        String dot = context.getResources().getString(R.string.dot);
        if (dotsCount == 1) {
            text = dot;
        } else if (dotsCount == 2) {
            text = dot + dot;
        } else if (dotsCount == 3) {
            text = dot + dot + dot;
        } else if (dotsCount > 3) {
            text = String.valueOf(dotsCount);
        }
        return text;
    }

    public Virtue getVirtue() {
        return virtue;
    }

    public void setVirtue(Virtue virtue) {
        this.virtue = virtue;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }
}
