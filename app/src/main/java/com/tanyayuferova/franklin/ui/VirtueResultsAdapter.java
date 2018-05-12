package com.tanyayuferova.franklin.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tanyayuferova.franklin.BR;
import com.tanyayuferova.franklin.R;
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

    private List<VirtueResult> data;

    public VirtueResultsAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VirtueResultsAdapter.ViewHolder(ItemVirtueResultBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
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
        return data.get(position).getVirtue().getId();
    }

    public void setData(List<VirtueResult> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemVirtueResultBinding binding;

        public ViewHolder(ItemVirtueResultBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(VirtueResult item) {
            binding.setItem(item);
        }
    }
}
