package com.example.my_scheduler.data;

import android.widget.TextView;

public class ScheduleRecyclerData {

    private String today_date;
    private String title_str;
    private String content_str;
    private String start_str;
    private String end_str;
    private String start_time;
    private String end_time;
    private String alarm_date;
    private String alarm_time;
    private String location;
    private String image_path;

    public ScheduleRecyclerData(String today_date, String title, String content, String start, String end) {
        this.today_date = today_date;
        this.title_str = title;
        this.content_str = content;
        this.start_str = start;
        this.end_str = end;
    }

    public ScheduleRecyclerData(String today_date, String title, String content, String start, String end,
                                String start_time, String end_time, String alarm_date, String alarm_time,
                                String location, String image_path) {
        this.today_date = today_date;
        this.title_str = title;
        this.content_str = content;
        this.start_str = start;
        this.end_str = end;
        this.start_time = start_time;
        this.end_time = end_time;
        this.alarm_date = alarm_date;
        this.alarm_time = alarm_time;
        this.location = location;
        this.image_path = image_path;
    }

    public String getToday_date() {
        return today_date;
    }

    public void setToday_date(String today_date) {
        this.today_date = today_date;
    }

    public String getTitle() {
        return this.title_str;
    }

    public void setTitle(String title) {
        title_str = title;
    }

    public String getEnd() {
        return this.end_str;
    }

    public void setEnd(String end) {
        end_str = end;
    }

    public String getStart() {
        return this.start_str;
    }

    public void setStart(String start) {
        start_str = start;
    }

    public String getContent() {
        return content_str;
    }

    public void setContent(String content_str) {
        this.content_str = content_str;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getAlarm_date() {
        return alarm_date;
    }

    public void setAlarm_date(String alarm_date) {
        this.alarm_date = alarm_date;
    }

    public String getAlarm_time() {
        return alarm_time;
    }

    public void setAlarm_time(String alarm_time) {
        this.alarm_time = alarm_time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}
