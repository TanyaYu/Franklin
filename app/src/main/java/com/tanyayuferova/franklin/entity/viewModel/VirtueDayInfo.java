package com.tanyayuferova.franklin.entity.viewModel;

import android.content.Context;

import com.tanyayuferova.franklin.R;
import com.tanyayuferova.franklin.entity.Virtue;

/**
 * Created by Tanya Yuferova on 3/4/2018.
 */

public class VirtueDayInfo {

    private int id;
    private String name;
    private String description;
    private String marks;

    public VirtueDayInfo() {}

    public VirtueDayInfo(Virtue virtue) {
        id = virtue.getId();
        name = virtue.getName();
        description = virtue.getDescription();
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }
}
