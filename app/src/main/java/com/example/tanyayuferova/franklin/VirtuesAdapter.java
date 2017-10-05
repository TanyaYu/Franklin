package com.example.tanyayuferova.franklin;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tanyayuferova.franklin.data.VirtuesContract;

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
        void onClick(long virtueId, int daysOff);
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
        holder.day1TV.setText(mCursor.getString(mCursor.getColumnIndex(MainActivity.DAY_1)));
        holder.day2TV.setText(mCursor.getString(mCursor.getColumnIndex(MainActivity.DAY_2)));
        holder.day3TV.setText(mCursor.getString(mCursor.getColumnIndex(MainActivity.DAY_3)));
        holder.day4TV.setText(mCursor.getString(mCursor.getColumnIndex(MainActivity.DAY_4)));
        holder.day5TV.setText(mCursor.getString(mCursor.getColumnIndex(MainActivity.DAY_5)));
        holder.day6TV.setText(mCursor.getString(mCursor.getColumnIndex(MainActivity.DAY_6)));
        holder.day7TV.setText(mCursor.getString(mCursor.getColumnIndex(MainActivity.DAY_7)));
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

    class VirtuesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        final TextView virtueName;
        final TextView day1TV;
        final TextView day2TV;
        final TextView day3TV;
        final TextView day4TV;
        final TextView day5TV;
        final TextView day6TV;
        final TextView day7TV;

        VirtuesAdapterViewHolder(View view) {
            super(view);
            virtueName = (TextView) view.findViewById(R.id.tv_virtue_name);
            day1TV = (TextView) view.findViewById(R.id.tv_day_1);
            day2TV = (TextView) view.findViewById(R.id.tv_day_2);
            day3TV = (TextView) view.findViewById(R.id.tv_day_3);
            day4TV = (TextView) view.findViewById(R.id.tv_day_4);
            day5TV = (TextView) view.findViewById(R.id.tv_day_5);
            day6TV = (TextView) view.findViewById(R.id.tv_day_6);
            day7TV = (TextView) view.findViewById(R.id.tv_day_7);

            day1TV.setOnClickListener(this);
            day2TV.setOnClickListener(this);
            day3TV.setOnClickListener(this);
            day4TV.setOnClickListener(this);
            day5TV.setOnClickListener(this);
            day6TV.setOnClickListener(this);
            day7TV.setOnClickListener(this);

            day1TV.setTag(0);
            day2TV.setTag(1);
            day3TV.setTag(2);
            day4TV.setTag(3);
            day5TV.setTag(4);
            day6TV.setTag(5);
            day7TV.setTag(6);
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
