package com.tanyayuferova.franklin.ui;

import android.content.Context;
import android.database.Cursor;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tanyayuferova.franklin.BR;
import com.tanyayuferova.franklin.databinding.ItemVirtueBinding;
import com.tanyayuferova.franklin.databinding.ItemVirtueSelectedBinding;
import com.tanyayuferova.franklin.entity.Virtue;
import com.tanyayuferova.franklin.entity.viewModel.VirtueDayInfo;

import static android.provider.BaseColumns._ID;
import static com.tanyayuferova.franklin.ui.VirtuesFragment.DAY_CODE;

/**
 * Created by Tanya Yuferova on 10/4/2017.
 */

public class VirtuesAdapter extends RecyclerView.Adapter<VirtuesAdapter.BaseVirtueViewHolder> {

    private final Context context;
    private Cursor mCursor;
    final private VirtuesAdapterOnClickHandler mClickHandler;
    private int selectedPosition = -1;
    private int TYPE_VIRTUE = 1;
    private int TYPE_SELECTED_VIRTUE = 2;

    public VirtuesAdapter(Context context, VirtuesAdapterOnClickHandler clickHandler) {
        this.context = context;
        mClickHandler = clickHandler;
    }

    @Override
    public BaseVirtueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_SELECTED_VIRTUE) {
            return new VirtueSelectedViewHolder(ItemVirtueSelectedBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
            ));
        } else {
            return new VirtueViewHolder(ItemVirtueBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
            ));
        }
    }

    @Override
    public void onBindViewHolder(BaseVirtueViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        Virtue virtue = Virtue.newVirtueById(context, mCursor.getInt(mCursor.getColumnIndex(_ID)));
        int dotsCount = mCursor.getInt(mCursor.getColumnIndex(DAY_CODE));

        holder.bind(new VirtueDayInfo(virtue, dotsCount, context));
    }

    @Override
    public long getItemId(int position) {
        mCursor.moveToPosition(position);
        return mCursor.getInt(mCursor.getColumnIndex(_ID));
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        //fixme when close cursor??
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position == selectedPosition ? TYPE_SELECTED_VIRTUE : TYPE_VIRTUE;
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
    public void selectVirtue(int selectedId) {
        setSelectedPosition(getItemPosition(selectedId));
    }

    // todo refactor
    private int getItemPosition(int itemId) {
        int position = -1;
        if (mCursor != null && mCursor.moveToFirst()) {
            do {
                if (mCursor.getInt(mCursor.getColumnIndex(_ID)) == itemId) {
                    position = mCursor.getPosition();
                    break;
                }
            } while (mCursor.moveToNext());
        }
        return position;
    }

    class BaseVirtueViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
        T binding;

        public BaseVirtueViewHolder(T binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(VirtueDayInfo item) {
            binding.setVariable(BR.item, item);
            binding.setVariable(BR.listener, mClickHandler);
        }
    }

    class VirtueViewHolder extends BaseVirtueViewHolder<ItemVirtueBinding> {
        VirtueViewHolder(ItemVirtueBinding binding) {
            super(binding);
        }
    }

    class VirtueSelectedViewHolder extends BaseVirtueViewHolder<ItemVirtueSelectedBinding> {
        VirtueSelectedViewHolder(ItemVirtueSelectedBinding binding) {
            super(binding);
        }
    }

    public interface VirtuesAdapterOnClickHandler {
        void onVirtueNameClick(int virtueId);

        void onDayClick(View view, int virtueId);

        boolean onDayLongClick(int virtueId);
    }
}
