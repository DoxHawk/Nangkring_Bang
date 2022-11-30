package com.example.nangkringbang.Model;

import java.util.List;

public class Model_Favorit {

    private String fav_nama, fav_type, fav_desk;
    private List<String> fav_img;

    public Model_Favorit () {}

    public Model_Favorit(String fav_nama, String fav_type, String fav_desk, List<String> fav_img) {
        this.fav_nama = fav_nama;
        this.fav_type = fav_type;
        this.fav_desk = fav_desk;
        this.fav_img = fav_img;
    }

    public String getFav_nama() {
        return fav_nama;
    }

    public void setFav_nama(String fav_nama) {
        this.fav_nama = fav_nama;
    }

    public String getFav_type() {
        return fav_type;
    }

    public void setFav_type(String fav_type) {
        this.fav_type = fav_type;
    }

    public String getFav_desk() {
        return fav_desk;
    }

    public void setFav_desk(String fav_desk) {
        this.fav_desk = fav_desk;
    }

    public List<String> getFav_img() {
        return fav_img;
    }

    public void setFav_img(List<String> fav_img) {
        this.fav_img = fav_img;
    }
}
