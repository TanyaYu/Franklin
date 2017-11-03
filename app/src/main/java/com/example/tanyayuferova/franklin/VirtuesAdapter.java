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
import android.widget.Toast;

import com.example.tanyayuferova.franklin.data.VirtuesContract;

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
        void onVirtueNameClick(String virtueName);
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
        String virtueShortName = mCursor.getString(MainActivity.MAIN_PROJECTION_SHORT_NAME_INDEX);
        holder.virtueName.setText(virtueShortName);

        /* Sets dots for every day */
        for (int i = 0; i < MainActivity.DAYS_COUNT; i++) {
            int dotsCount = mCursor.getInt(mCursor.getColumnIndex(MainActivity.DAY_CODE + i));
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
        final TextView[] daysTV = new TextView[MainActivity.DAYS_COUNT];

        VirtuesAdapterViewHolder(View view) {
            super(view);

            virtueName = (TextView) view.findViewById(R.id.tv_virtue_name);
            virtueName.setOnClickListener(this);

            for (int i = 0; i < MainActivity.DAYS_COUNT; i++) {
                daysTV[i] = createDayTextView(view, i);
            }
        }

        private TextView createDayTextView(View parent, int daysShiftTag) {
            //TODO ERROR ContextThemeWrapper
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

            /* Clicked on virtue name */
            if(R.id.tv_virtue_name == v.getId()){
                String virtueName = mCursor.getString(MainActivity.MAIN_PROJECTION_NAME_INDEX);
                mClickHandler.onVirtueNameClick(virtueName);
            } else { /* Clicked on day */
                mClickHandler.onDayClick(mCursor.getLong(mCursor.getColumnIndex(VirtuesContract.VirtueEntry._ID)),
                        (int) v.getTag());
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
