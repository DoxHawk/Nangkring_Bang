package com.example.nangkringbang.Model;

import java.util.List;

public class Model_Keranjang {

    private String cart_nama;
    private int cart_harga, cart_qty, cart_total;
    private List<String> cart_img;

    public Model_Keranjang() {}

    public Model_Keranjang(String cart_nama, int cart_harga, int cart_qty, int cart_total, List<String> cart_img) {
        this.cart_nama = cart_nama;
        this.cart_harga = cart_harga;
        this.cart_qty = cart_qty;
        this.cart_total = cart_total;
        this.cart_img = cart_img;
    }

    public String getCart_nama() {
        return cart_nama;
    }

    public void setCart_nama(String cart_nama) {
        this.cart_nama = cart_nama;
    }

    public int getCart_harga() {
        return cart_harga;
    }

    public void setCart_harga(int cart_harga) {
        this.cart_harga = cart_harga;
    }

    public int getCart_qty() {
        return cart_qty;
    }

    public void setCart_qty(int cart_qty) {
        this.cart_qty = cart_qty;
    }

    public int getCart_total() {
        return cart_total;
    }

    public void setCart_total(int cart_total) {
        this.cart_total = cart_total;
    }

    public List<String> getCart_img() {
        return cart_img;
    }

    public void setCart_img(List<String> cart_img) {
        this.cart_img = cart_img;
    }
}
