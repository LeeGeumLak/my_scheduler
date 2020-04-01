package com.example.my_scheduler.data;

public class SayingRecyclerData {
    public int img;
    public int wise_saying;
    public int saying_man;

    /*public SayingRecyclerData(int img, String wise_saying, String saying_man) {
        this.img = img;
        this.wise_saying = wise_saying;
        this.saying_man = saying_man;
    }*/

    public SayingRecyclerData(int wise_img, int wise_saying, int saying_man) {
        this.img = wise_img;
        this.wise_saying = wise_saying;
        this.saying_man = saying_man;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getImg() {
        return this.img;
    }

    public void setWise_saying(int wise_saying) {
        this.wise_saying = wise_saying;
    }

    public int getWiseSaying() {
        return this.wise_saying;
    }

    public void setSaying_man(int saying_man) {
        this.saying_man = saying_man;
    }

    public int getSayingMan() {
        return this.saying_man;
    }
}
