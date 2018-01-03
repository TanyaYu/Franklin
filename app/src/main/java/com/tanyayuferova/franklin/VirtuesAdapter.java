package com.tanyayuferova.franklin;

import android.content.Context;
import android.database.Cursor;
import android.view.ContextThemeWrapper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tanyayuferova.franklin.data.VirtuesContract;
import com.tanyayuferova.franklin.entity.Virtue;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by Tanya Yuferova on 10/4/2017.
 */

public class VirtuesAdapter extends RecyclerView.Adapter<VirtuesAdapter.VirtuesAdapterViewHolder> {
    private final Context mContext;
    private Cursor mCursor;
    final private VirtuesAdapterOnClickHandler mClickHandler;
    private int selectedPosition = -1;

    public VirtuesAdapter(Context mContext, VirtuesAdapterOnClickHandler clickHandler) {
        this.mContext = mContext;
        mClickHandler = clickHandler;
    }

    public interface VirtuesAdapterOnClickHandler {
        void onDayClick(long virtueId, int daysShift);
        void onVirtueNameClick(Virtue virtue);
        void onDayLongClick(long virtueId, int daysShift);
    }

    @Override
    public VirtuesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.virtues_list_item, parent, false);
        return new VirtuesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VirtuesAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String virtueShortName = mCursor.getString(WeekTableFragment.MAIN_PROJECTION_SHORT_NAME_INDEX);
        holder.virtueName.setText(virtueShortName);

        /* Sets dots for every day */
        for (int i = 0; i < WeekTableFragment.DAYS_COUNT; i++) {
            int dotsCount = mCursor.getInt(mCursor.getColumnIndex(WeekTableFragment.DAY_CODE + i));
            String text = "";
            String dot = mContext.getResources().getString(R.string.dot);
            if(dotsCount == 1) {
                text = dot;
            } else if(dotsCount == 2) {
                text = dot + dot;
            } else if(dotsCount == 3) {
                text = dot + dot + dot;
            } else if(dotsCount > 3){
                text = String.valueOf(dotsCount);
            }
            holder.daysTV[i].setText(text);
        }
        /* Highlight current week virtue */
        holder.itemView.setSelected(selectedPosition == position);
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

    class VirtuesAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        final TextView virtueName;
        final TextView[] daysTV = new TextView[WeekTableFragment.DAYS_COUNT];

        VirtuesAdapterViewHolder(View view) {
            super(view);

            virtueName = (TextView) view.findViewById(R.id.tv_virtue_name);
            virtueName.setOnClickListener(this);

            for (int i = 0; i < WeekTableFragment.DAYS_COUNT; i++) {
                daysTV[i] = createDayTextView(view, i);
            }
        }

        private TextView createDayTextView(View parent, int daysShiftTag) {
            TextView tv = new TextView(new ContextThemeWrapper(parent.getContext(), R.style.DayValueTextView));
            ((ViewGroup) parent).addView(tv, new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 1));
            tv.setOnClickListener(this);
            tv.setOnLongClickListener(this);
            tv.setTag(daysShiftTag);
            return tv;
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);

            Virtue virtue = new Virtue(mCursor.getInt(WeekTableFragment.MAIN_PROJECTION_ID_INDEX),
                    mCursor.getString(WeekTableFragment.MAIN_PROJECTION_NAME_INDEX),
                    mCursor.getString(WeekTableFragment.MAIN_PROJECTION_SHORT_NAME_INDEX),
                    mCursor.getString(WeekTableFragment.MAIN_PROJECTION_DESCRIPTION_INDEX));

            /* Clicked on virtue name */
            if(R.id.tv_virtue_name == v.getId()){
                mClickHandler.onVirtueNameClick(virtue);
            } else { /* Clicked on day */
                mClickHandler.onDayClick(virtue.getId(), (int) v.getTag());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            mClickHandler.onDayLongClick(mCursor.getLong(mCursor.getColumnIndex(VirtuesContract.VirtueEntry._ID)),
                        (int) v.getTag());
            return true;
        }
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    /**
     * Finds virtue position with id in cursor and sets it as selected position
     * @param selectedId virtue id
     */
    public void setSelectedId(int selectedId){
        int position = -1;
        if (mCursor != null) {
            int idIndex = mCursor.getColumnIndex(VirtuesContract.VirtueEntry._ID);
            if (mCursor.moveToFirst()) {
                do {
                    if (mCursor.getInt(idIndex) == selectedId) {
                        position = mCursor.getPosition();
                        break;
                    }
                } while (mCursor.moveToNext());
            }
        }
        setSelectedPosition(position);
    }
}
