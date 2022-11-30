package com.example.nangkringbang.Model;

public class Ulasan {
    private String imgUser;
    private String namaUser;
    private float rating;
    private String review;
    private String tanggal;

    public String getNamaUser() {
        return this.namaUser;
    }

    public void setNamaUser(String namaUser2) {
        this.namaUser = namaUser2;
    }

    public String getTanggal() {
        return this.tanggal;
    }

    public void setTanggal(String tanggal2) {
        this.tanggal = tanggal2;
    }

    public String getReview() {
        return this.review;
    }

    public void setReview(String review2) {
        this.review = review2;
    }

    public String getImgUser() {
        return this.imgUser;
    }

    public void setImgUser(String imgUser2) {
        this.imgUser = imgUser2;
    }

    public float getRating() {
        return this.rating;
    }

    public void setRating(float rating2) {
        this.rating = rating2;
    }
}
