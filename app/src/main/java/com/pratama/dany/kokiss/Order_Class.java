package com.pratama.dany.kokiss;

public class Order_Class {

    private int index;
    private String id_plg;
    private String nama_plg;
    private int nomeja_plg;
    private String status_plg;

    public Order_Class(int index, String id_plg, String nama_plg, int nomeja_plg, String status_plg){
        this.index = index;
        this.id_plg = id_plg;
        this.nama_plg = nama_plg;
        this.nomeja_plg = nomeja_plg;
        this.status_plg = status_plg;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getId_plg() {
        return id_plg;
    }

    public void setId_plg(String id_plg) {
        this.id_plg = id_plg;
    }

    public String getNama_plg() {
        return nama_plg;
    }

    public void setNama_plg(String nama_plg) {
        this.nama_plg = nama_plg;
    }

    public int getNomeja_plg() {
        return nomeja_plg;
    }

    public void setNomeja_plg(int nomeja_plg) {
        this.nomeja_plg = nomeja_plg;
    }

    public String getStatus_plg() {
        return status_plg;
    }

    public void setStatus_plg(String status_plg) {
        this.status_plg = status_plg;
    }
}
