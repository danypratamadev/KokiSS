package com.pratama.dany.kokiss;

public class Sayur_Class {

    private String id_mnu_sy;
    private String nama_sy;
    private String status_sy;
    private int harga_sy;
    private String img_sy;

    public Sayur_Class(String id_mnu_sy, String img_sy, String nama_sy, String status_sy, int harga_sy){
        this.id_mnu_sy = id_mnu_sy;
        this.nama_sy = nama_sy;
        this.status_sy = status_sy;
        this.harga_sy = harga_sy;
        this.img_sy = img_sy;
    }

    public String getId_mnu_sy() {
        return id_mnu_sy;
    }

    public void setId_mnu_sy(String id_mnu_sy) {
        this.id_mnu_sy = id_mnu_sy;
    }

    public String getImg_sy() {
        return img_sy;
    }

    public void setImg_sy(String img_sy) {
        this.img_sy = img_sy;
    }

    public String getNama_sy() {
        return nama_sy;
    }

    public void setNama_sy(String nama_sy) {
        this.nama_sy = nama_sy;
    }

    public String getStatus_sy() {
        return status_sy;
    }

    public void setHarga_sy(int harga_sy) {
        this.harga_sy = harga_sy;
    }

    public int getHarga_sy() {
        return harga_sy;
    }

    public void setStatus_sy(String status_sy) {
        this.status_sy = status_sy;
    }
}
