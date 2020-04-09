package com.example.my_scheduler.data;

public class MemoDiaryData {
    private String today_date;
    private String title_str;
    private String content_str;

    public MemoDiaryData(String date, String title, String content) {
        this.today_date = date;
        this.title_str = title;
        this.content_str = content;
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

    public String getContent() {
        return content_str;
    }

    public void setContent(String content_str) {
        this.content_str = content_str;
    }
}
