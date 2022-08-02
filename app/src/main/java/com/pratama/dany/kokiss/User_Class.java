package com.pratama.dany.kokiss;

public class User_Class {

    private String id_usr;
    private String img_usr;
    private String nm_usr;
    private String email;
    private String akses;

    public User_Class(String id_usr, String img_usr, String nm_usr, String email, String akses){
        this.id_usr = id_usr;
        this.img_usr = img_usr;
        this.nm_usr = nm_usr;
        this.email = email;
        this.akses = akses;
    }

    public String getId_usr() {
        return id_usr;
    }

    public void setId_usr(String id_usr) {
        this.id_usr = id_usr;
    }

    public String getImg_usr() {
        return img_usr;
    }

    public void setImg_usr(String img_usr) {
        this.img_usr = img_usr;
    }

    public String getNm_usr() {
        return nm_usr;
    }

    public void setNm_usr(String nm_usr) {
        this.nm_usr = nm_usr;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAkses() {
        return akses;
    }

    public void setAkses(String akses) {
        this.akses = akses;
    }

}
