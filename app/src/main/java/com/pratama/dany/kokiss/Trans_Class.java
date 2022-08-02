package com.pratama.dany.kokiss;

public class Trans_Class {

    private int index;
    private String id_trans;
    private String nama_trans;
    private String month;
    private String day;

    public Trans_Class(int index, String id_trans, String nama_trans, String month, String day){
        this.index = index;
        this.id_trans = id_trans;
        this.nama_trans = nama_trans;
        this.month = month;
        this.day = day;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getId_trans() {
        return id_trans;
    }

    public void setId_trans(String id_trans) {
        this.id_trans = id_trans;
    }

    public String getNama_trans() {
        return nama_trans;
    }

    public void setNama_trans(String nama_trans) {
        this.nama_trans = nama_trans;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
