package com.pratama.dany.kokiss;

public class Minum_Class {

    private String id_mnu_mn;
    private String nama_mn;
    private String status_mn;
    private int harga_mn;
    private String img_mn;

    public Minum_Class(String id_mnu_mn, String img_mn, String nama_mn, String status_mn, int harga_mn){
        this.id_mnu_mn = id_mnu_mn;
        this.nama_mn = nama_mn;
        this.status_mn = status_mn;
        this.harga_mn = harga_mn;
        this.img_mn = img_mn;
    }

    public String getId_mnu_mn() {
        return id_mnu_mn;
    }

    public void setId_mnu_mn(String id_mnu_mn) {
        this.id_mnu_mn = id_mnu_mn;
    }

    public String getImg_mn() {
        return img_mn;
    }

    public void setImg_mn(String img_mn) {
        this.img_mn = img_mn;
    }

    public String getNama_mn() {
        return nama_mn;
    }

    public void setNama_mn(String nama_mn) {
        this.nama_mn = nama_mn;
    }

    public String getStatus_mn() {
        return status_mn;
    }

    public void setHarga_mn(int harga_mn) {
        this.harga_mn = harga_mn;
    }

    public int getHarga_mn() {
        return harga_mn;
    }

    public void setStatus_mn(String status_mn) {
        this.status_mn = status_mn;
    }

}
