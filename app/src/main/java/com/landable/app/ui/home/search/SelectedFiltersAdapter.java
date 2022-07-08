package com.landable.app.ui.home.search;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.landable.app.R;
import com.landable.app.common.SelectedFilterSearch;
import com.landable.app.databinding.SelectedFacesSearchLayoutBinding;

import java.util.ArrayList;

public class SelectedFiltersAdapter extends RecyclerView.Adapter<SelectedFiltersAdapter.MyViewHolder> {
    private final Activity activity;
    private final SelectedFilterSearch listener;
    private final ArrayList<String> filtersArrayList;

    public SelectedFiltersAdapter(Activity activity,
                                  ArrayList<String> filtersArrayList, SelectedFilterSearch listener) {
        this.filtersArrayList = filtersArrayList;
        this.activity = activity;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SelectedFiltersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        SelectedFacesSearchLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.selected_faces_search_layout, parent, false);
        return new SelectedFiltersAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectedFiltersAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            //holder.setIsRecyclable(false);


            holder.facesBinding.tvName.setText(filtersArrayList.get(position));

            holder.facesBinding.ivRemoveFace.setOnClickListener(view ->
                    listener.onFilterSelected("remove",filtersArrayList.get(position)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return filtersArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        SelectedFacesSearchLayoutBinding facesBinding;

        MyViewHolder(SelectedFacesSearchLayoutBinding facesBinding) {
            super(facesBinding.getRoot());
            this.facesBinding = facesBinding;
        }
    }


}