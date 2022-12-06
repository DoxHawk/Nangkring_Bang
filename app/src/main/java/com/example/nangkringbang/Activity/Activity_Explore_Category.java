package com.example.nangkringbang.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nangkringbang.Adapter.Adapter_Category_Full;
import com.example.nangkringbang.Model.Model_Kategori;
import com.example.nangkringbang.databinding.ActivityViewBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Activity_Explore_Category extends AppCompatActivity {

    private ActivityViewBinding main;
    private TextView txtTitle;
    private RecyclerView recyclerView;
    private DisplayMetrics metrics;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Query query;
    private Adapter_Category_Full adpt_c;

    private static final String CATEGORY = "kategori";
    private static final String TAG = "Activity_Explore_Categories";

    @Override
    public void onStart() {
        super.onStart();
        adpt_c.startListening();
        adpt_c.notifyDataSetChanged();
        recyclerView.getRecycledViewPool().clear();
    }

    @Override
    public void onPause() {
        super.onPause();
        adpt_c.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        adpt_c.startListening();
        adpt_c.notifyDataSetChanged();
        recyclerView.getRecycledViewPool().clear();
    }

    @Override
    public void onStop() {
        super.onStop();
        adpt_c.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = ActivityViewBinding.inflate(getLayoutInflater());
        setContentView(main.getRoot());

        txtTitle = main.txtViewTitle;
        recyclerView = main.recyclerView11;
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        txtTitle.setText("Categories");

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        if (mUser != null) {
            if (getIntent() != null) {
                query = firebaseFirestore.collection(CATEGORY);

                FirestoreRecyclerOptions<Model_Kategori> options = new FirestoreRecyclerOptions.Builder<Model_Kategori>()
                        .setQuery(query, Model_Kategori.class)
                        .build();

                GridLayoutManager layoutManager = new GridLayoutManager(Activity_Explore_Category.this, 2, GridLayoutManager.VERTICAL, false);
                adpt_c = new Adapter_Category_Full(options, metrics);
                recyclerView.setAdapter(adpt_c);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setNestedScrollingEnabled(true);
            }

            adpt_c.setOnItemClickListener(new Adapter_Category_Full.OnItemClickListener() {
                @Override
                public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                    Intent intent;
                    if (documentSnapshot != null) {
                        Model_Kategori model = documentSnapshot.toObject(Model_Kategori.class);
                        if (model != null) {
                            if (model.getKategori_type().equals("menu")) {
                                intent = new Intent(Activity_Explore_Category.this, Activity_Category_Menus.class);
                                intent.putExtra("KEY_CATEGORY_DETAIL_NAME", model.getKategori_name());
                                intent.putExtra("KEY_CATEGORY_DETAIL_ID", documentSnapshot.getId());
                                startActivity(intent);
                            } else if (model.getKategori_type().equals("tempat")) {
                                intent = new Intent(Activity_Explore_Category.this, Activity_Category_Places.class);
                                intent.putExtra("KEY_CATEGORY_DETAIL_NAME", model.getKategori_name());
                                intent.putExtra("KEY_CATEGORY_DETAIL_ID", documentSnapshot.getId());
                                startActivity(intent);
                            }
                        }
                    }
                }
            });
        }
    }
}
