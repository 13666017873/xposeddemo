package com.example.administrator.xposeddemo.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class TimeBean extends LitePalSupport implements Serializable {

    private String time;
    private int id;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
