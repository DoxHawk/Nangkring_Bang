package com.example.nangkringbang.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nangkringbang.Adapter.Adapter_Orders;
import com.example.nangkringbang.Function.AutoFitGridLayoutManager;
import com.example.nangkringbang.Model.Model_Pesanan;
import com.example.nangkringbang.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class Fragment_Orders_List extends Fragment {
    
    private View view;
    private Context context;
    private String status;
    private RecyclerView recyclerView;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Query query;
    private static final String PROFILE = "users";
    private static final String ORDERS = "pesanan";
    private Adapter_Orders adpt_o;

    public Fragment_Orders_List(String a) {
        this.status = a;
    }

    @Override
    public void onStart() {
        super.onStart();
        adpt_o.startListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        adpt_o.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        adpt_o.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adpt_o.stopListening();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view                = inflater.inflate(R.layout.recycler, container, false);
        recyclerView        = view.findViewById(R.id.recyclerView8);

        firebaseFirestore   = FirebaseFirestore.getInstance();
        mAuth               = FirebaseAuth.getInstance();
        mUser               = mAuth.getCurrentUser();
        transaksi();
        return view;
    }

    private void transaksi() {
        if (mUser != null){
            query = firebaseFirestore.collection(PROFILE).document(mUser.getUid())
                    .collection(ORDERS).whereEqualTo("pesanan_status", status).orderBy("pesanan_tgl_order", Query.Direction.DESCENDING);
            FirestoreRecyclerOptions<Model_Pesanan> options = new FirestoreRecyclerOptions.Builder<Model_Pesanan>()
                    .setQuery(query, Model_Pesanan.class)
                    .build();

            AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(Fragment_Orders_List.this.context);
            adpt_o = new Adapter_Orders(options);
            recyclerView.setAdapter(adpt_o);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setNestedScrollingEnabled(true);
        }
    }
    
}
