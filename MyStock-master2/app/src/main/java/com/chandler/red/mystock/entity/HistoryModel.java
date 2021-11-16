package com.chandler.red.mystock.entity;

public class HistoryModel {
    public String day;//这里存储日期
    public String close;//这里存储收盘价

    public HistoryModel(String day, String close) {
        this.day = day;
        this.close = close;
    }
}
