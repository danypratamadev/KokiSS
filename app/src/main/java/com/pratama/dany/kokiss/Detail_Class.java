package com.pratama.dany.kokiss;

public class Detail_Class {

    private String detail_id;
    private String detail_img;
    private String detail_nm_mnu;
    private String detail_ktgr_mnu;
    private int detail_jml_mnu;

    public Detail_Class(String detail_id, String detail_img, String detail_nm_mnu, String detail_ktgr_mnu, int detail_jml_mnu){
        this.detail_id = detail_id;
        this.detail_img = detail_img;
        this.detail_nm_mnu = detail_nm_mnu;
        this.detail_ktgr_mnu = detail_ktgr_mnu;
        this.detail_jml_mnu = detail_jml_mnu;


    }

    public String getDetail_id() {

        return detail_id;

    }

    public void setDetail_id(String detail_id) {

        this.detail_id = detail_id;

    }

    public String getDetail_img() {

        return detail_img;

    }

    public void setDetail_img(String detail_img) {

        this.detail_img = detail_img;

    }

    public String getDetail_nm_mnu(){

        return detail_nm_mnu;

    }

    public void setDetail_nm_mnu(String detail_nm_mnu) {

        this.detail_nm_mnu = detail_nm_mnu;

    }

    public String getDetail_ktgr_mnu() {
        return detail_ktgr_mnu;
    }

    public void setDetail_ktgr_mnu(String detail_ktgr_mnu) {
        this.detail_ktgr_mnu = detail_ktgr_mnu;
    }

    public int getDetail_jml_mnu(){

        return detail_jml_mnu;

    }

    public void setDetail_jml_mnu(int detail_jml_mnu) {

        this.detail_jml_mnu = detail_jml_mnu;

    }

}
