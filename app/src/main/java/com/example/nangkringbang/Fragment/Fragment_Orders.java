package com.example.nangkringbang.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.nangkringbang.Adapter.Adapter_Menu;
import com.example.nangkringbang.Adapter.Adapter_Orders;
import com.example.nangkringbang.Adapter.Adapter_Orders_List;
import com.example.nangkringbang.Function.AutoFitGridLayoutManager;
import com.example.nangkringbang.Model.Model_Menu;
import com.example.nangkringbang.Model.Model_Pesanan;
import com.example.nangkringbang.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Orders extends Fragment {

    private View view;
    private Context context;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Adapter_Orders_List adapter_orders_list;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view            = inflater.inflate(R.layout.fragment_transaksi, container, false);
        viewPager       = view.findViewById(R.id.view_pager);
        tabLayout       = view.findViewById(R.id.tab_layout);
        context         = view.getContext();
        adapter_orders_list = new Adapter_Orders_List(getChildFragmentManager());

        viewPager.setAdapter(adapter_orders_list);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }
}
