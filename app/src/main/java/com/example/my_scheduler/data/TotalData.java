package com.example.my_scheduler.data;

import androidx.recyclerview.widget.RecyclerView;

public class TotalData {
    private RecyclerView schedule_recycler;

    public void setSchedule_recycler(RecyclerView schedule_recycler) {
        this.schedule_recycler = schedule_recycler;
    }

    public RecyclerView getSchedule_recycler() {
        return schedule_recycler;
    }
}
