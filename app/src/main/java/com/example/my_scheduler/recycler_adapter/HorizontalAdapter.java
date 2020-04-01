package com.example.my_scheduler.recycler_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_scheduler.R;
import com.example.my_scheduler.data.ScheduleRecyclerData;
import com.example.my_scheduler.data.TotalData;

import java.util.ArrayList;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.HorizontalViewHolder> {

    private ArrayList<ArrayList<ScheduleRecyclerData>> dataList;
    private Context context;

    public HorizontalAdapter(Context context, ArrayList<ArrayList<ScheduleRecyclerData>> data)
    {
        this.context = context;
        this.dataList = data;
    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder{
        protected RecyclerView recyclerView;

        public HorizontalViewHolder(View view) {
            super(view);
            recyclerView = view.findViewById(R.id.recyclerViewVertical);
        }
    }

    @NonNull
    @Override
    public HorizontalViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_total_item, null);

        return new HorizontalAdapter.HorizontalViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HorizontalViewHolder horizontalViewHolder, int position) {

        ScheduleRecyclerAdapter adapter = new ScheduleRecyclerAdapter(dataList.get(position));

        horizontalViewHolder.recyclerView.setHasFixedSize(true);
        horizontalViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        horizontalViewHolder.recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
