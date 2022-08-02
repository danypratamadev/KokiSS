package com.pratama.dany.kokiss;

public class Lauk_Class {

    private String id_mnu_lk;
    private String nama_lk;
    private String status_lk;
    private int harga_lk;
    private String img_lk;

    public Lauk_Class(String id_mnu_lk, String img_lk, String nama_lk, String status_lk, int harga_lk){
        this.id_mnu_lk = id_mnu_lk;
        this.nama_lk = nama_lk;
        this.status_lk = status_lk;
        this.harga_lk = harga_lk;
        this.img_lk = img_lk;
    }

    public String getId_mnu_lk() {
        return id_mnu_lk;
    }

    public void setId_mnu_lk(String id_mnu_lk) {
        this.id_mnu_lk = id_mnu_lk;
    }

    public String getNama_lk(){

        return nama_lk;

    }

    public void setNama_lk(String nama_lk) {

        this.nama_lk = nama_lk;

    }

    public String getStatus_lk(){

        return status_lk;

    }

    public void setStatus_lk(String status_lk) {

        this.status_lk = status_lk;

    }

    public int getHarga_lk(){

        return harga_lk;

    }

    public void setHarga_lk(int harga_lk) {

        this.harga_lk = harga_lk;

    }

    public String getImg_lk() {

        return img_lk;

    }

    public void setImg_lk(String img_lk) {

        this.img_lk = img_lk;

    }

}
