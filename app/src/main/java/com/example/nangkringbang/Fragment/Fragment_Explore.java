package com.example.nangkringbang.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nangkringbang.Activity.Activity_Menu_Detail;
import com.example.nangkringbang.Activity.Activity_Tempat_Detail;
import com.example.nangkringbang.Adapter.Adapter_Category;
import com.example.nangkringbang.Adapter.Adapter_Menu;
import com.example.nangkringbang.Adapter.Adapter_Places;
import com.example.nangkringbang.Model.Model_Kategori;
import com.example.nangkringbang.Model.Model_Menu;
import com.example.nangkringbang.Model.Model_Tempat;
import com.example.nangkringbang.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class Fragment_Explore extends Fragment {

    private View view;
    private Context context;
    private RecyclerView recyclerView1, recyclerView2, recyclerView3;
    private Adapter_Menu adpt_m;
    private Adapter_Places adpt_t;
    private Adapter_Category adpt_c;
    private DisplayMetrics metrics;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Query query1, query2, query3;

    private static final String MENU = "menu";
    private static final String PLACES = "tempat";
    private static final String CATEGORY = "kategori";
    private static final String TAG = "Fragment_Explore";

    public Fragment_Explore() {
    }

    @Override
    public void onStart() {
        super.onStart();
        adpt_m.startListening();
        adpt_t.startListening();
        adpt_c.startListening();
        adpt_m.notifyDataSetChanged();
        adpt_t.notifyDataSetChanged();
        adpt_c.notifyDataSetChanged();
        recyclerView1.getRecycledViewPool().clear();
        recyclerView2.getRecycledViewPool().clear();
        recyclerView3.getRecycledViewPool().clear();
    }

    @Override
    public void onPause() {
        super.onPause();
        adpt_m.stopListening();
        adpt_t.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        adpt_m.startListening();
        adpt_t.startListening();
        adpt_c.startListening();
        adpt_m.notifyDataSetChanged();
        adpt_t.notifyDataSetChanged();
        adpt_c.notifyDataSetChanged();
        recyclerView1.getRecycledViewPool().clear();
        recyclerView2.getRecycledViewPool().clear();
        recyclerView3.getRecycledViewPool().clear();
    }

    @Override
    public void onStop() {
        super.onStop();
        adpt_m.stopListening();
        adpt_t.stopListening();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_explore, container, false);
        context = view.getContext();
        recyclerView1 = view.findViewById(R.id.recyclerView7);
        recyclerView2 = view.findViewById(R.id.recyclerView8);
        recyclerView3 = view.findViewById(R.id.recyclerView10);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        getMenus();
        getPlace();
        getCategory();
        return view;
    }

    private void getMenus() {
        query1 = firebaseFirestore.collection(MENU).orderBy("menu_harga", Query.Direction.DESCENDING).limit(4);
        FirestoreRecyclerOptions<Model_Menu> options = new FirestoreRecyclerOptions.Builder<Model_Menu>()
                .setQuery(query1, Model_Menu.class)
                .build();

//        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(Fragment_Home.this.getActivity());
        GridLayoutManager layoutManager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
        adpt_m = new Adapter_Menu(options, mUser);
        recyclerView2.setAdapter(adpt_m);
        recyclerView2.setLayoutManager(layoutManager);
        recyclerView2.setNestedScrollingEnabled(true);

        adpt_m.setOnItemClickListener(new Adapter_Menu.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                if (documentSnapshot != null) {
                    Intent menuIntent = new Intent(Fragment_Explore.this.getActivity(), Activity_Menu_Detail.class);
                    menuIntent.putExtra("KEY_MENU_ID", documentSnapshot.getId());
                    if (menuIntent.resolveActivity(Fragment_Explore.this.getActivity().getPackageManager()) != null) {
                        Fragment_Explore.this.getActivity().startActivity(menuIntent);
                    }
                }
            }
        });
    }

    private void getPlace() {
        query2 = firebaseFirestore.collection(PLACES);
        FirestoreRecyclerOptions<Model_Tempat> options = new FirestoreRecyclerOptions.Builder<Model_Tempat>()
                .setQuery(query2, Model_Tempat.class)
                .build();

        LinearLayoutManager layoutManager = new LinearLayoutManager(Fragment_Explore.this.getActivity(), LinearLayoutManager.HORIZONTAL, false);
        adpt_t = new Adapter_Places(options, metrics);
        recyclerView1.setAdapter(adpt_t);
        recyclerView1.setLayoutManager(layoutManager);
        recyclerView1.setNestedScrollingEnabled(true);

        adpt_t.setOnItemClickListener(new Adapter_Places.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                if (documentSnapshot != null) {
                    Intent placeIntent = new Intent(Fragment_Explore.this.getActivity(), Activity_Tempat_Detail.class);
                    placeIntent.putExtra("KEY_TEMPAT_ID", documentSnapshot.getId());
                    if (placeIntent.resolveActivity(Fragment_Explore.this.getActivity().getPackageManager()) != null) {
                        Fragment_Explore.this.getActivity().startActivity(placeIntent);
                    }
                }
            }
        });
    }

    private void getCategory() {
        query3 = firebaseFirestore.collection(CATEGORY);
        FirestoreRecyclerOptions<Model_Kategori> options = new FirestoreRecyclerOptions.Builder<Model_Kategori>()
                .setQuery(query3, Model_Kategori.class)
                .build();

        LinearLayoutManager layoutManager = new LinearLayoutManager(Fragment_Explore.this.getActivity(), LinearLayoutManager.HORIZONTAL, false);
        adpt_c = new Adapter_Category(options, metrics);
        recyclerView3.setAdapter(adpt_c);
        recyclerView3.setLayoutManager(layoutManager);
        recyclerView3.setNestedScrollingEnabled(true);
    }
}
