package com.example.nangkringbang.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.nangkringbang.Fragment.Fragment_Orders_List;

public class Adapter_Orders_List extends FragmentStatePagerAdapter {

    // tab titles
    private String[] titles = new String[]{"On Going", "Compleated", "Cancel"};

    public Adapter_Orders_List(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Fragment_Orders_List("Proses");
            case 1:
                return new Fragment_Orders_List("Selesai");
            case 2:
                return new Fragment_Orders_List("Batal");
        }
        return new Fragment_Orders_List("Batal");
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }
}
