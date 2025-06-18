package com.example.appdoctruyen;

import java.io.Serializable;

public class Chapter implements Serializable {
    private int number;
    private String url;

    public Chapter(int number, String url) {
        this.number = number;
        this.url = url;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}