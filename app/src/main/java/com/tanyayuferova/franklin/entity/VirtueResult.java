package com.tanyayuferova.franklin.entity;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

import com.tanyayuferova.franklin.R;

/**
 * Created by Tanya Yuferova on 4/25/2018.
 */

public class VirtueResult {
    private Virtue virtue;
    private int marksCount;
    private Result result;
    private Result progress;

    public VirtueResult(Virtue virtue, int marksCount, Result result, Result progress) {
        this.virtue = virtue;
        this.marksCount = marksCount;
        this.result = result;
        this.progress = progress;
    }

    public Virtue getVirtue() {
        return virtue;
    }

    public void setVirtue(Virtue virtue) {
        this.virtue = virtue;
    }

    public int getMarksCount() {
        return marksCount;
    }

    public void setMarksCount(int marksCount) {
        this.marksCount = marksCount;
    }

    public String getMarks() {
        return String.valueOf(marksCount);
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Result getProgress() {
        return progress;
    }

    public void setProgress(Result progress) {
        this.progress = progress;
    }

    public @DrawableRes
    int getProgressIcon() {
        switch (progress) {
            case POSITIVE:
                return R.drawable.icon_arrow_up;
            case NEGATIVE:
                return R.drawable.icon_arrow_down;
            case NEUTRAL:
            default:
                return 0;
        }
    }

    public @ColorRes
    int getProgressColor() {
        switch (progress) {
            case POSITIVE:
                return R.color.result_positive;
            case NEGATIVE:
                return R.color.result_negative;
            case NEUTRAL:
            default:
                return R.color.result_neutral;
        }
    }

    public @ColorRes
    int getResultColor() {
        switch (result) {
            case POSITIVE:
                return R.color.result_positive;
            case NEGATIVE:
                return R.color.result_negative;
            case NEUTRAL:
            default:
                return R.color.result_neutral;
        }
    }
}
