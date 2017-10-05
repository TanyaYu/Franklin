package com.example.tanyayuferova.franklin;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tanyayuferova.franklin.data.VirtuesContract;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by Tanya Yuferova on 10/4/2017.
 */

public class VirtuesAdapter extends RecyclerView.Adapter<VirtuesAdapter.VirtuesAdapterViewHolder> {
    private final Context mContext;
    private Cursor mCursor;
    final private VirtuesAdapterOnClickHandler mClickHandler;

    public VirtuesAdapter(Context mContext, VirtuesAdapterOnClickHandler clickHandler) {
        this.mContext = mContext;
        mClickHandler = clickHandler;
    }

    public interface VirtuesAdapterOnClickHandler {
        void onClick(long virtueId, int daysShift);
    }

    @Override
    public VirtuesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.virtues_list_item, parent, false);
        return new VirtuesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VirtuesAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.virtueName.setText(mCursor.getString(mCursor.getColumnIndex(VirtuesContract.VirtueEntry.COLUMN_SHORT_NAME)));
        for (int i = 0; i < MainActivity.DAYS_COUNT; i++) {
            holder.daysTV[i].setText(mCursor.getString(mCursor.getColumnIndex(MainActivity.DAY_CODE + i)));
        }
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    class VirtuesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView virtueName;
        final TextView[] daysTV = new TextView[MainActivity.DAYS_COUNT];

        VirtuesAdapterViewHolder(View view) {
            super(view);
            virtueName = (TextView) view.findViewById(R.id.tv_virtue_name);
            for (int i = 0; i < MainActivity.DAYS_COUNT; i++) {
                daysTV[i] = createDayTextView(view, i);
            }
        }

        private TextView createDayTextView(View parent, int daysShiftTag) {
            TextView tv = new TextView(new ContextThemeWrapper(parent.getContext(), R.style.DayValueTextView));
            ((ViewGroup) parent).addView(tv, new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 1));
            tv.setOnClickListener(this);
            tv.setTag(daysShiftTag);
            return tv;
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            mClickHandler.onClick(mCursor.getLong(mCursor.getColumnIndex(VirtuesContract.VirtueEntry._ID)),
                    (int) v.getTag());
        }
    }
}
