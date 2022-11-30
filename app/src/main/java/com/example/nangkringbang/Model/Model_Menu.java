package com.example.nangkringbang.Model;

import java.util.List;

public class Model_Menu {
    private String menu_nama, menu_desk, menu_owner, menu_kategori;
    private int menu_harga, menu_stok;
    private List<String> menu_img;

    public Model_Menu() {}

    public Model_Menu(String menu_nama, String menu_desk, List<String> menu_img, String menu_owner, String menu_kategori, int menu_harga, int menu_stok) {
        this.menu_nama = menu_nama;
        this.menu_desk = menu_desk;
        this.menu_img = menu_img;
        this.menu_owner = menu_owner;
        this.menu_kategori = menu_kategori;
        this.menu_harga = menu_harga;
        this.menu_stok = menu_stok;
    }

    public String getMenu_nama() {
        return menu_nama;
    }

    public void setMenu_nama(String menu_nama) {
        this.menu_nama = menu_nama;
    }

    public String getMenu_desk() {
        return menu_desk;
    }

    public void setMenu_desk(String menu_desk) {
        this.menu_desk = menu_desk;
    }

    public List<String> getMenu_img() {
        return menu_img;
    }

    public void setMenu_img(List<String> menu_img) {
        this.menu_img = menu_img;
    }

    public String getMenu_owner() {
        return menu_owner;
    }

    public void setMenu_owner(String menu_owner) {
        this.menu_owner = menu_owner;
    }

    public String getMenu_kategori() {
        return menu_kategori;
    }

    public void setMenu_kategori(String menu_kategori) {
        this.menu_kategori = menu_kategori;
    }

    public int getMenu_harga() {
        return menu_harga;
    }

    public void setMenu_harga(int menu_harga) {
        this.menu_harga = menu_harga;
    }

    public int getMenu_stok() {
        return menu_stok;
    }

    public void setMenu_stok(int menu_stok) {
        this.menu_stok = menu_stok;
    }
}
