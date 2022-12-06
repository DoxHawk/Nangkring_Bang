package com.example.nangkringbang.Model;

import java.util.List;

public class Model_Tempat {

    private String tempat_email, tempat_lokasi, tempat_nama, tempat_owner, tempat_status, tempat_telp, tempat_buka, tempat_tutup;;
    private List<String> tempat_img;

    public Model_Tempat () {}

    public Model_Tempat(String tempat_email, String tempat_lokasi, String tempat_nama, String tempat_owner, String tempat_status, String tempat_telp, String tempat_buka, String tempat_tutup, List<String> tempat_img) {
        this.tempat_email = tempat_email;
        this.tempat_lokasi = tempat_lokasi;
        this.tempat_nama = tempat_nama;
        this.tempat_owner = tempat_owner;
        this.tempat_status = tempat_status;
        this.tempat_telp = tempat_telp;
        this.tempat_buka = tempat_buka;
        this.tempat_tutup = tempat_tutup;
        this.tempat_img = tempat_img;
    }

    public String getTempat_email() {
        return tempat_email;
    }

    public void setTempat_email(String tempat_email) {
        this.tempat_email = tempat_email;
    }

    public String getTempat_lokasi() {
        return tempat_lokasi;
    }

    public void setTempat_lokasi(String tempat_lokasi) {
        this.tempat_lokasi = tempat_lokasi;
    }

    public String getTempat_nama() {
        return tempat_nama;
    }

    public void setTempat_nama(String tempat_nama) {
        this.tempat_nama = tempat_nama;
    }

    public String getTempat_owner() {
        return tempat_owner;
    }

    public void setTempat_owner(String tempat_owner) {
        this.tempat_owner = tempat_owner;
    }

    public String getTempat_status() {
        return tempat_status;
    }

    public void setTempat_status(String tempat_status) {
        this.tempat_status = tempat_status;
    }

    public String getTempat_telp() {
        return tempat_telp;
    }

    public void setTempat_telp(String tempat_telp) {
        this.tempat_telp = tempat_telp;
    }

    public String getTempat_buka() {
        return tempat_buka;
    }

    public void setTempat_buka(String tempat_buka) {
        this.tempat_buka = tempat_buka;
    }

    public String getTempat_tutup() {
        return tempat_tutup;
    }

    public void setTempat_tutup(String tempat_tutup) {
        this.tempat_tutup = tempat_tutup;
    }

    public List<String> getTempat_img() {
        return tempat_img;
    }

    public void setTempat_img(List<String> tempat_img) {
        this.tempat_img = tempat_img;
    }
}
