package com.tanyayuferova.franklin.ui;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tanyayuferova.franklin.BR;
import com.tanyayuferova.franklin.R;
import com.tanyayuferova.franklin.databinding.ItemSubtitleBinding;
import com.tanyayuferova.franklin.databinding.ItemVirtueResultBinding;
import com.tanyayuferova.franklin.entity.Result;
import com.tanyayuferova.franklin.entity.Virtue;
import com.tanyayuferova.franklin.entity.VirtueResult;

import java.util.List;
import java.util.Random;

/**
 * Created by Tanya Yuferova on 4/25/2018.
 */

public class VirtueResultsAdapter extends RecyclerView.Adapter<VirtueResultsAdapter.ViewHolder>  {

    private List<Object> data;
    private int SUBTITLE_ITEM_TYPE = 1;
    private int VIRTUE_ITEM_TYPE = 2;

    public VirtueResultsAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == SUBTITLE_ITEM_TYPE)
            return new VirtueResultsAdapter.ViewHolder(ItemSubtitleBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false
            ));
        if(viewType == VIRTUE_ITEM_TYPE)
        return new VirtueResultsAdapter.ViewHolder(ItemVirtueResultBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
        else throw new UnsupportedOperationException("Unknown type");
    }

    @Override
    public int getItemViewType(int position) {
        if(data.get(position) instanceof String)
            return SUBTITLE_ITEM_TYPE;
        if (data.get(position) instanceof VirtueResult)
            return VIRTUE_ITEM_TYPE;
        else throw new UnsupportedOperationException("Unknown type");
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(List<Object> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewDataBinding binding;

        public ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Object item) {
            binding.setVariable(BR.item, item);
        }
    }
}
