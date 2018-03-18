package com.tanyayuferova.franklin.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tanyayuferova.franklin.R;
import com.tanyayuferova.franklin.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by Tanya Yuferova on 3/17/2018.
 */

public class DaySelectorWidget extends LinearLayout {

    public static String DEFAULT_DATE_FORMAT = "DDD";

    private int daysCount = 1;
    private SimpleDateFormat dateFormat;
    private Date firstDate = new Date();
    private int selectedIndex = -1;
    private OnDayClickedListener onDayClickedListener;

    public DaySelectorWidget(Context context) {
        this(context, null);
    }

    public DaySelectorWidget(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DaySelectorWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DaySelectorWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr, defStyleRes);
    }

    private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        setOrientation(HORIZONTAL);

        if (attrs != null) {
            final TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.DaySelectorWidget, defStyleAttr, defStyleRes);
            daysCount = array.getInt(R.styleable.DaySelectorWidget_daysCount, 1);
            String format = array.getString(R.styleable.DaySelectorWidget_dateFormat);
            dateFormat = new SimpleDateFormat(
                    format == null ? DEFAULT_DATE_FORMAT : format,
                    getResources().getConfiguration().locale
            );
            array.recycle();

        }
        initLayout();
    }

    private void initLayout() {
        removeAllViews();
        for (int i = 0; i < daysCount; i++) {
            TextView view = (TextView) inflate(getContext(), R.layout.item_day, null);

            final int index = i;
            final Date date = DateUtils.addDaysToDate(firstDate, i);
            String text = dateFormat.format(date);
            boolean isToday = DateUtils.isToday(date);
            int textColor = getResources().getColor(isToday ? R.color.colorAccent : R.color.colorPrimaryText);

            view.setText(text);
            view.setTextColor(textColor);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    onDateClick(date, index);
                }
            });

            addView(view, i, new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 1));
        }
    }

    private void onDateClick(Date date, int index) {
        selectDateAtIndex(index);

        if (onDayClickedListener != null) {
            onDayClickedListener.onDateClicked(date);
        }
    }

    public void selectDate(Date date) {
//fixme need refactoring
        int index = -1;
        for (int i = 0; i < daysCount; i++) {
            Date d = DateUtils.addDaysToDate(firstDate, i);
            if (DateUtils.isTheSameDay(date, d))
                index = i;
        }
        selectDateAtIndex(index);
    }

    public void selectDateAtIndex(int index) {
        selectedIndex = index;
        performSelection();
    }

    private void performSelection() {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setSelected(false);
        }

        View view = getChildAt(selectedIndex);
        if (view != null)
            view.setSelected(true);
    }

    @Override
    protected void onAttachedToWindow() {
        performSelection();
        super.onAttachedToWindow();
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        SavedState state = new SavedState(super.onSaveInstanceState());
        state.selectedIndex = selectedIndex;
        return state;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());

        this.selectedIndex = savedState.selectedIndex;
    }

    public int getDaysCount() {
        return daysCount;
    }

    public void setDaysCount(int daysCount) {
        this.daysCount = daysCount;
        selectedIndex = -1;
        initLayout();
    }

    public Date getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(Date firstDate) {
        this.firstDate = firstDate;
        selectedIndex = -1;
        initLayout();
    }

    public OnDayClickedListener getOnDayClickedListener() {
        return onDayClickedListener;
    }

    public void setOnDayClickedListener(OnDayClickedListener onDayClickedListener) {
        this.onDayClickedListener = onDayClickedListener;
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    static class SavedState extends BaseSavedState {
        int selectedIndex;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.selectedIndex = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.selectedIndex);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

    public interface OnDayClickedListener {
        void onDateClicked(Date date);
    }
}
