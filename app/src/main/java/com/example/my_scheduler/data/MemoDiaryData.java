package com.example.my_scheduler.data;

public class MemoDiaryData {
    public String title_str;
    public String content_str;

    public MemoDiaryData(String title, String content) {
        this.title_str = title;
        this.content_str = content;
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
