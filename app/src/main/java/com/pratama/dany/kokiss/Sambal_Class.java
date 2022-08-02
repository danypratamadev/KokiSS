package com.pratama.dany.kokiss;

public class Sambal_Class {

    private String id_mnu_sb;
    private String nama_sb;
    private String status_sb;
    private int harga_sb;
    private String img_sb;

    public Sambal_Class(String id_mnu_sb, String img_sb, String nama_sb, String status_sb, int harga_sb){
        this.id_mnu_sb = id_mnu_sb;
        this.nama_sb = nama_sb;
        this.status_sb = status_sb;
        this.harga_sb = harga_sb;
        this.img_sb = img_sb;
    }

    public String getId_mnu_sb() {
        return id_mnu_sb;
    }

    public void setId_mnu_sb(String id_mnu_lk) {
        this.id_mnu_sb = id_mnu_lk;
    }

    public String getNama_sb(){

        return nama_sb;

    }

    public void setNama_sb(String nama_sb) {

        this.nama_sb = nama_sb;

    }

    public String getStatus_sb(){

        return status_sb;

    }

    public void setStatus_sb(String status_sb) {

        this.status_sb = status_sb;

    }

    public int getHarga_sb(){

        return harga_sb;

    }

    public void setHarga_sb(int harga_sb) {

        this.harga_sb = harga_sb;

    }

    public String getImg_sb() {

        return img_sb;

    }

    public void setImg_sb(String img_sb) {

        this.img_sb = img_sb;

    }

}
