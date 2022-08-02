package com.pratama.dany.kokiss;

public class Bayar_Class {

    private String bayar_id;
    private String bayar_nm_mnu;
    private int bayar_jml_mnu;
    private int bayar_hrg_mnu;
    private int bayar_tot_all;

    public Bayar_Class(String bayar_id, String bayar_nm_mnu, int bayar_jml_mnu, int bayar_hrg_mnu, int bayar_tot_all){
        this.bayar_id = bayar_id;
        this.bayar_nm_mnu = bayar_nm_mnu;
        this.bayar_jml_mnu = bayar_jml_mnu;
        this.bayar_tot_all = bayar_tot_all;
        this.bayar_hrg_mnu = bayar_hrg_mnu;
    }

    public String getBayar_id() {
        return bayar_id;
    }

    public void setBayar_id(String bayar_id) {
        this.bayar_id = bayar_id;
    }

    public String getBayar_nm_mnu() {
        return bayar_nm_mnu;
    }

    public void setBayar_nm_mnu(String bayar_nm_mnu) {
        this.bayar_nm_mnu = bayar_nm_mnu;
    }

    public int getBayar_jml_mnu() {
        return bayar_jml_mnu;
    }

    public void setBayar_jml_mnu(int bayar_jml_mnu) {
        this.bayar_jml_mnu = bayar_jml_mnu;
    }

    public int getBayar_hrg_mnu() {
        return bayar_hrg_mnu;
    }

    public void setBayar_hrg_mnu(int bayar_hrg_mnu) {
        this.bayar_hrg_mnu = bayar_hrg_mnu;
    }

    public int getBayar_tot_all() {
        return bayar_tot_all;
    }

    public void setBayar_tot_all(int bayar_tot_all) {
        this.bayar_tot_all = bayar_tot_all;
    }
}
