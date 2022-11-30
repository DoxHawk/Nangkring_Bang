package com.example.nangkringbang.Model;

import java.util.List;

public class Model_Menu_Detail {

    private String detail_nama;
    private int detail_harga, detail_qty, detail_total;
    private List<String> detail_img;

    public Model_Menu_Detail () {}

    public Model_Menu_Detail(String detail_nama, int detail_harga, int detail_qty, int detail_total, List<String> detail_img) {
        this.detail_nama = detail_nama;
        this.detail_harga = detail_harga;
        this.detail_qty = detail_qty;
        this.detail_total = detail_total;
        this.detail_img = detail_img;
    }

    public String getDetail_nama() {
        return detail_nama;
    }

    public void setDetail_nama(String detail_nama) {
        this.detail_nama = detail_nama;
    }

    public int getDetail_harga() {
        return detail_harga;
    }

    public void setDetail_harga(int detail_harga) {
        this.detail_harga = detail_harga;
    }

    public int getDetail_qty() {
        return detail_qty;
    }

    public void setDetail_qty(int detail_qty) {
        this.detail_qty = detail_qty;
    }

    public int getDetail_total() {
        return detail_total;
    }

    public void setDetail_total(int detail_total) {
        this.detail_total = detail_total;
    }

    public List<String> getDetail_img() {
        return detail_img;
    }

    public void setDetail_img(List<String> detail_img) {
        this.detail_img = detail_img;
    }
}
